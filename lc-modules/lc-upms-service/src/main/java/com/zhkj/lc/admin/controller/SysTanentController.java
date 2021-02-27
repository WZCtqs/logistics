package com.zhkj.lc.admin.controller;
import java.util.*;

import com.sun.glass.ui.Menu;
import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.util.StrUtil;
import com.zhkj.lc.admin.common.util.PswGenerator;
import com.zhkj.lc.admin.common.util.TreeUtil;
import com.zhkj.lc.admin.feign.DriverFeign;
import com.zhkj.lc.admin.mapper.SysRoleDeptMapper;
import com.zhkj.lc.admin.mapper.SysTanentMapper;
import com.zhkj.lc.admin.model.dto.MenuTree;
import com.zhkj.lc.admin.model.dto.TanentDTO;
import com.zhkj.lc.admin.model.dto.UserDTO;
import com.zhkj.lc.admin.model.entity.*;
import com.zhkj.lc.admin.service.*;
import com.zhkj.lc.common.util.StringUtils;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.TanentVo;
import io.swagger.annotations.ApiOperation;
//import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.zhkj.lc.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhkj.lc.common.util.Query;
import com.zhkj.lc.common.util.R;
import com.zhkj.lc.common.web.BaseController;

/**
 * <p>
 * 系统租户信息表 前端控制器
 * </p>
 *
 * @author cb
 * @since 2018-12-13
 */
@RestController
@RequestMapping("/sysTanent")
public class SysTanentController extends BaseController {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired private SysTanentService sysTanentService;

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysDeptRelationService sysDeptRelationService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private DriverFeign driverFeign;

    @Autowired
    private SysSmsTempService sysSmsTempService;

    @Autowired
    private SysDictService sysDictService;
    /**
    * 通过ID查询
    *
    * @param tenantId ID
    * @return SysTanent
    */
    @ApiOperation(value = "根据租户id获取租户数据", notes = "根据租户id获取租户数据")
    @GetMapping("/{tenantId}")
    public R<SysTanent> get(@PathVariable Integer tenantId) {
        return new R<>(sysTanentService.selectTanentInfo(tenantId));
    }

    @GetMapping("/getTenantShortName/{tenantId}")
    public String getTenantShortName(@PathVariable("tenantId")Integer tenantId){
        return sysTanentService.selectTenantShortName(tenantId);
    }

    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "获取分页的租户数据", notes = "获取分页的租户数据，需要tanentName,status,expriedTime三个筛选框")
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params, TanentVo tanentVo) {
      //  params.put("tanentName", tanentVo.getTanentName());
        return sysTanentService.selectTanentVoPage(new Query<>(params), tanentVo);
    }

    /**
     * 添加
     * @param
     * @return success/false
     */
    @Transactional
    @PostMapping("/addTanent")
    @ApiOperation(value = "添加租户信息", notes = "添加租户信息，菜单id是通过选择菜单树得来的string字符串")
    public R<Boolean> add(@RequestBody TanentDTO tanentDTO) {
        String phone = tanentDTO.getPhone();
        Boolean f = sysTanentService.findPhone(phone);
        UserDTO user = new UserDTO();
        user.setPhone(phone);
        Boolean u = sysUserService.findPhone(user);
            if(f && u){
                SysTanent sysTanent = new SysTanent();
                BeanUtils.copyProperties(tanentDTO, sysTanent);
                sysTanent.setCreateTime(new Date());
                sysTanent.setCreateBy("超级管理员");
                //设置随机密码
                sysTanent.setOldPsw(PswGenerator.getPswRandom(12));
                //设置账号为激活状态
                sysTanent.setStatus(CommonConstant.TANENT_NORMAL);
                sysTanentService.insertTanentInfo(sysTanent);
                //1、获取新增的租户id后开始进行租户最高机构的构建
                Integer roleId = buildDeptOfTanent(sysTanent);
                //获取选中的菜单id
                String menuIds = sysTanent.getMenuIds();
                Integer tenantId = sysTanent.getTenantId();

                //2、若为合作方，给租户新增客户信息：郑州国际陆港
                if(tanentDTO.getIsPartner().equals("1")){
                    addCustomer(tenantId);
                }

                //3、新增借款利率
                driverFeign.insertRate(tenantId);

                //4、新增短信发送设置
                setSmsTpl(tenantId);

                //5、新增字典数据
                setDictData(tenantId);

                //更新角色菜单表
                return new R<>( sysRoleMenuService.insertRoleMenus(roleId, menuIds, tenantId));
            }
            else {
                return new R<>(Boolean.FALSE,"手机号已被注册！");
            }



    }

    private void addCustomer(Integer tenantId) {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setTenantId(tenantId);
        customerVO.setIsTrust(CommonConstant.STATUS_NORMAL);
        customerVO.setCustomerName("郑州国际陆港开发建设有限公司");
        driverFeign.addTanentCustomer(customerVO);

    }

    /**
     * 建立该租户的最高机构
     * @param sysTanent
     */
    public Integer buildDeptOfTanent(SysTanent sysTanent){
        SysDept sysDept = new SysDept();
        //将部门的name命名为租户name
        sysDept.setName(sysTanent.getTanentName());
        //设置机构的租户id
        sysDept.setTenantId(sysTanent.getTenantId());
        //设置该机构为最高机构
        sysDept.setParentId(0);
        //设置创建时间
        sysDept.setCreateTime(new Date());
        //保存该机构
        sysDeptService.insertDeptInfo(sysDept);
        //更新部门关系表
        sysDeptService.insertDeptRelation(sysDept);
        //为租户新建一个角色,并获取roleId
        Integer roleId= buildRoleOfTanent(sysTanent, sysDept);
        return  roleId;
    }

    /**
     * 建立租户的最高管理员的角色，并与租户对应的机构绑定
     * @param sysTanent
     * @param sysDept
     */
    public Integer buildRoleOfTanent(SysTanent sysTanent,SysDept sysDept){
        SysRole sysRole  = new SysRole();
        //设置角色的租户id
        sysRole.setTenantId(sysTanent.getTenantId());
        //设置角色的name
        sysRole.setRoleName("admin");
        //设置角色的role_code
        sysRole.setRoleCode("ROLE_ADMIN"+sysTanent.getTenantId());
        //设置角色的描述
        sysRole.setRoleDesc("管理员");
        //设置角色的创建时间
        sysRole.setCreateTime(new Date());
        //保存租户的该角色
        sysRoleService.insertRoleInfo(sysRole);
        //获取角色的id
        Integer roleId = sysRole.getRoleId();

        //更新角色部门表
        SysRoleDept sysRoleDept = new SysRoleDept();
        sysRoleDept.setTenantId(sysTanent.getTenantId());
        sysRoleDept.setRoleId(roleId);
        sysRoleDept.setDeptId(sysDept.getDeptId());
        sysRoleDeptMapper.insert(sysRoleDept);

        //新建一个租户的超级管理员账号
        buildUserOfTanent(sysRole, sysTanent, sysDept);
        //返回角色id
        return  roleId;
    }

    public void buildUserOfTanent(SysRole sysRole,SysTanent sysTanent,SysDept sysDept){

        Integer tenantId = sysTanent.getTenantId();
        SysUser sysUser = new SysUser();
        //设置user的租户id
        sysUser.setTenantId(tenantId);
        //设置user的部门id
        sysUser.setDeptId(sysDept.getDeptId());
        //设置user的name为手机号,不能为中文
        sysUser.setUsername(sysTanent.getPhone());
        //设置密码为初始密码
        sysUser.setPassword(ENCODER.encode(sysTanent.getOldPsw()));
        //设置user的创建时间
        sysUser.setCreateTime(new Date());
        //设置手机号
        sysUser.setPhone(sysTanent.getPhone());
        //设置用户状态激活
        sysUser.setStatus(CommonConstant.TANENT_NORMAL);
        sysUserService.insertUserInfo(sysUser);
        //获取userid
        Integer userId = sysUser.getUserId();
        //更新用户角色表
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setTenantId(tenantId);
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(sysRole.getRoleId());
        sysUserRole.insert();

    }
    /**
     * 删除
     * @param
     * @return success/false
     */
    @ApiOperation(value = "根据租户id删除租户", notes = "根据租户id删除租户")
    @Transactional
    @DeleteMapping("/{tenantId}")
    public R<Boolean> delete(@PathVariable Integer tenantId) {
        SysTanent sysTanent = new SysTanent();
        sysTanent.setTenantId(tenantId);
        sysTanent.setUpdateTime(new Date());
        sysTanent.setDelFlag(CommonConstant.STATUS_DEL);
        //删除租户下的所有用户和部门以及角色
        SysUser sysUser = new SysUser();
        sysUser.setTenantId(tenantId);
        sysUserService.delete(new EntityWrapper<>(sysUser));
        //删除部门
        SysDept sysDept = new SysDept();
        sysDept.setTenantId(tenantId);
        sysDeptService.delete(new EntityWrapper<>(sysDept));
        //删除角色
        SysRole sysRole = new SysRole();
        sysRole.setTenantId(tenantId);
        sysRoleService.delete(new EntityWrapper<>(sysRole));
        //删除部门角色表
        SysRoleDept sysRoleDept = new SysRoleDept();
        sysRoleDept.setTenantId(tenantId);
        sysRoleDeptService.delete(new EntityWrapper<>(sysRoleDept));
        //删除用户角色表
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setTenantId(tenantId);
        sysUserRoleService.delete(new EntityWrapper<>(sysUserRole));
        return new R<>(sysTanentService.updateById(sysTanent));
    }


    @ApiOperation(value = "根据租户id禁用当前租户", notes = "根据租户id禁用当前租户")
     @Transactional
    @GetMapping("/forbidden/{tenantId}")
    public R<Boolean> forbidden(@PathVariable Integer tenantId){
        SysTanent sysTanent = new SysTanent();
        sysTanent.setTenantId(tenantId);
        sysTanent.setUpdateTime(new Date());
        //设置租户状态未激活
        sysTanent.setStatus(CommonConstant.TANENT_UNNORMAL);
        //更新租户下的用户状态
        fbdUserStatus(tenantId);
        return new R<>(sysTanentService.updateById(sysTanent));
    }

    @ApiOperation(value = "根据租户id激活当前租户", notes = "根据租户id激活当前租户")
    @Transactional
    @GetMapping("/active/{tenantId}")
    public R<Boolean> active(@PathVariable Integer tenantId){
        SysTanent sysTanent = new SysTanent();
        sysTanent.setTenantId(tenantId);
        sysTanent.setUpdateTime(new Date());
        //设置租户状态激活
        sysTanent.setStatus(CommonConstant.TANENT_NORMAL);
        //更新租户下的用户状态为激活
        UpUserStatus(tenantId);
        return new R<>(sysTanentService.updateById(sysTanent));
    }

    /**
     * 禁用租户下的账号
     * @param tenantId
     */
    private void fbdUserStatus(Integer tenantId) {
        //根据租户id查询用户表改租户下的用户
        SysUser sysUser = new SysUser();
        sysUser.setTenantId(tenantId);

        List<SysUser> users = sysUserService.selectList(new EntityWrapper<SysUser>(sysUser));
        for (SysUser user: users
             ) {
            user.setStatus(CommonConstant.TANENT_UNNORMAL);

        }
        sysUserService.updateBatchById(users);
    }

    /**
     * 激活租户下的账号
     * @param tenantId
     */
    private void UpUserStatus(Integer tenantId) {
        //根据租户id查询用户表改租户下的用户
        SysUser sysUser = new SysUser();
        sysUser.setTenantId(tenantId);
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        List<SysUser> users = sysUserService.selectList(new EntityWrapper<SysUser>(sysUser));
        for (SysUser user: users
                ) {
            user.setStatus(CommonConstant.TANENT_NORMAL);

        }
        sysUserService.updateBatchById(users);
    }


    /**
     * 编辑
     * @param  sysTanent  实体
     * @return success/false
     */
    @ApiOperation(value = "编辑保存租户数据", notes = "编辑保存租户数据")
    @PutMapping
    @Transactional
    public R<Boolean> edit(@RequestBody SysTanent sysTanent) {
        String phone = sysTanent.getPhone();
        Boolean f = sysTanentService.fingByPhone(phone, sysTanent.getTenantId());
        if(f) {
            sysTanent.setUpdateTime(new Date());
            sysTanent.setCreateBy("超级管理员");
            //删除对应的角色菜单表
            SysRoleMenu condition = new SysRoleMenu();
            condition.setRoleId(sysTanent.getRoleId());
            sysRoleMenuService.delete(new EntityWrapper<>(condition));
            if (StrUtil.isBlank(sysTanent.getMenuIds())) {
                return new R<>(Boolean.TRUE);
            }
            //更新角色菜单表
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            List<String> menuids = Arrays.asList(sysTanent.getMenuIds().split(","));
            for (String menuId : menuids) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(Integer.valueOf(menuId));
                sysRoleMenu.setRoleId(sysTanent.getRoleId());
                sysRoleMenu.setTenantId(sysTanent.getTenantId());
                sysRoleMenus.add(sysRoleMenu);
            }
            sysRoleMenuService.insertBatch(sysRoleMenus);
            return new R<>(sysTanentService.updateById(sysTanent));
        }else {
            return new R<>(Boolean.FALSE,"手机号已被注册！");
        }
    }

    /**
     * 获取菜单树供新增租户时调用
     */
    @ApiOperation(value = "获取所有菜单的菜单树", notes = "获取所有菜单的菜单树")
    @GetMapping(value = "/allTree")
    public List<MenuTree> getTree() {
        SysMenu condition = new SysMenu();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return TreeUtil.bulidTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1);
    }

    /**
     * 根据选择的租户id获取到该租户分配的菜单树
     */
    @ApiOperation(value = "获取当前租户的菜单集合", notes = "获取当前租户的菜单集合")
    @GetMapping("/tanentTree/{tenantId}")
    public List<Integer> getTanentTreeById(@PathVariable Integer tenantId){
        List<Integer> menuList = new ArrayList<>();
        //根据当前租户id获取租户的权限菜单
        if(sysTanentService.selectById(tenantId).getMenuIds()!=null){
            String[]menuids = sysTanentService.selectById(tenantId).getMenuIds().split(",");
            Integer[]menuIds = new Integer[menuids.length];
            for(int i= 0;i<menuids.length;i++){
                menuIds[i] = Integer.valueOf(menuids[i]);
            }
            menuList = Arrays.asList(menuIds);
        }

        return menuList;
    }

    /**
     * 不分页查询全部租户
     * @return
     */
    @PostMapping("selectTanentList")
    public List<TanentVo> selectTanentList(){
        return sysTanentService.selectTanentList();
    }

    /**
     * 根据weixinId查找租户
     */
    @PostMapping("selectByWA/{weixinId}")
    public TanentVo selectByWA(@PathVariable String weixinId){
        return sysTanentService.selectByWA(weixinId);
    }

    /**
     * 新增租户的时候将短信模板进行复制给租户
     */
    public void setSmsTpl(Integer tenantId){
        List<SysSmsTemp> smsTemps = sysSmsTempService.selectForTenant();
        for(SysSmsTemp s: smsTemps){
            s.setTenantId(tenantId);
            sysSmsTempService.insert(s);
        }
    }

    /**
     * 新增租户的时候创建其字典数据
     */
    public void setDictData(Integer tenantId){
        List<SysDict> dictList = sysDictService.selectSystemDict();
        for(SysDict dict: dictList){
            dict.setTenantId(tenantId);
            sysDictService.insert(dict);
        }
    }
}
