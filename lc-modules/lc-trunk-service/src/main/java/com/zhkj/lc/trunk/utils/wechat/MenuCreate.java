package com.zhkj.lc.trunk.utils.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;

public class MenuCreate {
	private static final String HEADER = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxacb74f7aa406c6e2&redirect_uri=http://21x91010v5.iok.la/wechatFront/";

	private static final String FOOT = "&response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect";

	private static final String NEWORDER = "showOrder?tenantId=0";

	private static final String SEEORDER = "showWaybill?tenantId=0";

	private static final String PERSONAL = "showPersonal?tenantId=0";

	private static final String TOKEN = "20_i3DAJJMM6y6t5TVXqzR8FF6Nl9lBRF1FJmLFQfZJ2Qy-q17IrxzbjNZa5UL27ytNmHZ2psjtCCk4xPotccEaLpbbsU1nDY995ZPB8Rx6zB5Vzp2ll1yC3yH7jrjxLHo5UKxJaZ5elZVtD464ONZeAEAZYK";


	Logger log = Logger.getLogger(MenuCreate.class);

	public static void main(String[] args) {

		MenuCreate menuCreate = new MenuCreate();
		try {
			String json = menuCreate.MenuInfo1();
			menuCreate.CreateMenu(json,TOKEN);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// 创建自定义菜单
	private void CreateMenu(String json, String accessToken) {
		try {
			URL url = new URL("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken);
			URLConnection conn = url.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(json.getBytes("utf-8"));
			outputStream.flush();

			InputStream inputStream = conn.getInputStream();
			byte[] b = new byte[1024];
			inputStream.read(b);
			System.out.println("接收数据" + new String(b));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 编辑自定义菜单内容
	private String MenuInfo1() throws UnsupportedEncodingException {
		String SCOPE = "snsapi_userinfo";
		// -------------------------------------------------------
		Button button1 = new Button();
		button1.setName("下单");
		button1.setType("view");
		button1.setUrl(HEADER+NEWORDER+FOOT);
		// ---------------------------------------------
		Button button2 = new Button();
		button2.setName("查看");
		button2.setType("view");
		button2.setUrl(HEADER+SEEORDER+FOOT);
		// ---------------------------------------------
		Button button3 = new Button();
		button3.setName("个人中心");
		button3.setType("view");
		button3.setUrl(HEADER+PERSONAL+FOOT);
		// ----------------------------------
		Button[] Buttons = new Button[3];
		Buttons[0] = button1;
		Buttons[1] = button2;
		Buttons[2] = button3;

		data data = new data();
		data.setButton(Buttons);

		// 使用Gson将对象类转成Json对象时=号出现u003d的问题
		String json = new GsonBuilder().disableHtmlEscaping().create().toJson(data, data.getClass());
		System.out.println(json);
		return json;
	}
}

class data {
	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}

}

class Button {
	private String type;
	private String name;
	private String key;
	private String url;
	private Button[] sub_button;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}

}
