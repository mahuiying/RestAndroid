package com.utopia.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.utopia.Model.d_Area;
import com.utopia.Model.d_Bill;
import com.utopia.Model.d_Cashier;
import com.utopia.Model.d_CashierInfor;
import com.utopia.Model.d_Contact;
import com.utopia.Model.d_Customer;
import com.utopia.Model.d_Desk;
import com.utopia.Model.d_MenuType;
import com.utopia.Model.d_Product;
import com.utopia.Model.d_Sale;
import com.utopia.Model.d_SaleRecord;
import com.utopia.Model.d_Saleandpdt;
import com.utopia.Model.d_Staff;
import com.utopia.Model.d_Tax;
import com.utopia.Model.d_pay_in_out;
import com.utopia.activity.R;
import com.utopia.widget.MyTextView;

public class JsonResolveUtils {
	public static String BASEURL = "";
	private Context context;

	public JsonResolveUtils(Context context) {
		this.context = context;
		// sql_Setting ss = new sql_Setting(context);
		// BASEURL = "http://192.168.1.249:8080";
		// BASEURL = "http://211.86.109.107:8080";// 综合楼
		BASEURL = "http://139.196.197.253"; // 阿里
		// BASEURL = "http://104.131.173.202";//国外云端
		// BASEURL = "http://192.168.1.117:8080";
		// BASEURL = "http://192.168.1.123:8080";

		// BASEURL = "http://192.168.23.1:8080";
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public boolean CheckLogin(Context context, String s_account) {
		boolean ret = false;
		String url = BASEURL + "/Backoffice/UserLogin";
		String params = "s_account=" + s_account;
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");

			if (jsonObject.getString("ret").equals("online")) {
				showCustomToast("Online!");
			}
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONObject object = dataObject.getJSONObject("staff");
				if (object.length() > 0) {
					String s_pwd = object.getString("SPwd");
					String s_name = object.getString("SName");
					String type_name = object.getString("typeName");
					int priority = object.optInt("priority");
					Constant.currentStaff = new d_Staff(s_account, s_pwd,
							s_name, type_name, priority);
				}
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(context).inflate(
				R.layout.common_toast, null);
		((MyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	public List<d_Tax> getTax() {
		List<d_Tax> taxs = new ArrayList<d_Tax>();
		d_Tax tax = new d_Tax();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetTax";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				JSONArray objectArr = dataObject.getJSONArray("taxs");

				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);
					tax = new d_Tax(rs.optInt("id"), rs.optString("taxeName"),
							rs.optDouble("rate"));

					taxs.add(tax);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxs;
	}

	/**
	 * 设置优先级
	 */
	public boolean setPriority(String itemNo, int priority) {
		boolean ret = false;
		String url = BASEURL
				+ "/Backoffice/SetSaleAndPdtPriority?salerecordId=" + itemNo
				+ "&priority=" + priority;
		Log.i("tag", "url" + url);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 记录分帐单信息
	 * 
	 * @param context
	 * @return
	 */
	public boolean sendSaleRecords(List<d_Sale> sales) {
		boolean ret = false;
		String result = "[";
		for (int i = 0; i < sales.size(); i++) {
			if (!result.equals("["))
				result += ",";
			result += sales.get(i).getString();
			Log.i("tag", sales.get(i).getString());
		}
		result += "]";
		List<Parameter> params = new ArrayList<Parameter>();
		String url = BASEURL + "/Backoffice/SetSaleRecord";
		params.add(new Parameter("saleRecords", result));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendSaleandpdt(List<d_Saleandpdt> sales) {
		boolean ret = false;
		String result = "[";
		for (int i = 0; i < sales.size(); i++) {
			if (!result.equals("["))
				result += ",";
			result += sales.get(i).getString();
			Log.i("tag", sales.get(i).getString());
		}
		result += "]";
		List<Parameter> params = new ArrayList<Parameter>();
		String url = BASEURL + "/Backoffice/SetSaleAndPdt";
		params.add(new Parameter("saleAndPdts", result));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public List<d_Sale> getSaleRecords() {
		List<d_Sale> sales = new ArrayList<d_Sale>();
		d_Sale sale = new d_Sale();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetSaleRecord";
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("createTime", Constant.lastTime));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");

			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("saleRecord");
				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);

					sale = new d_Sale(rs.optString("itemNo"),
							rs.optString("closeTime"),
							rs.optString("createTime"),
							rs.optString("deskName"),
							rs.optString("otherSpec"),
							rs.optString("otherSpecNo1"),
							rs.optString("otherSpecNo2"),
							rs.optString("status"), rs.optString("dept"),
							(float) rs.optDouble("subTotal"),
							(float) rs.optDouble("tipTotal"),
							(float) rs.optDouble("total"),
							(float) rs.optDouble("initTotal"),
							(float) rs.optDouble("rebate"),
							(float) rs.optDouble("taxTotal"),
							rs.optString("waiter"),
							(float) rs.optDouble("cashTotal"),
							(float) rs.optDouble("cardTotal"),
							rs.optInt("customerId"));
					sales.add(sale);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sales;
	}

	public List<d_Saleandpdt> getSaleandpdt() {
		List<d_Saleandpdt> sales = new ArrayList<d_Saleandpdt>();
		d_Saleandpdt sale = new d_Saleandpdt();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetSaleAndPdt";
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("createTime", Constant.lastTime));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");

			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("saleAndPdt");
				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);
					sale = new d_Saleandpdt(rs.optInt("id"),
							rs.optString("salerecordId"),
							rs.optString("pdtCode"), rs.optString("pdtName"),
							rs.optInt("number"), (float) rs.optDouble("price"),
							rs.optString("otherspec"),
							rs.optString("otherspec1"),
							rs.optString("otherspec2"), rs.optInt("priority"),
							rs.optString("status"), rs.optString("createTime"),
							rs.optString("closeTime"),
							rs.optInt("contactNumber"));
					sales.add(sale);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sales;
	}

	/**
	 * 网络测试
	 * 
	 * @param context
	 * @param s_account
	 * @return
	 */
	public boolean CheckService(Context context) {
		boolean ret = false;
		String url = BASEURL + "/Backoffice/Test";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public List<d_Product> getMenus() {
		List<d_Product> menus = new ArrayList<d_Product>();
		d_Product menu = new d_Product();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetMenus";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");

			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("menus");
				JSONObject obj = null;

				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					menu = new d_Product(obj.getString("departId"),
							obj.getString("pdtAccType"),
							obj.optInt("pdtAutoInc"), obj.optInt("pdtCanUsed"),
							obj.optInt("pdtCanZk"),
							obj.optInt("pdtChangePrice"),
							obj.getString("pdtCode"), obj.getString("pdtGg"),
							obj.getString("pdtID"), obj.optInt("pdtInMix"),
							Double.parseDouble(obj.optString("pdtInPrice")
									+ "0"), obj.getString("pdtMCode"),
							obj.optInt("pdtMakeTime"),
							obj.optString("pdtName"), obj.optInt("pdtNoShow"),
							obj.getDouble("pdtPayType"),
							obj.getString("pdtPy"), Float.parseFloat(obj
									.optString("pdtSalePrice1") + "0"),
							Double.parseDouble(obj.optString("pdtSalePrice2")
									+ "0"), obj.getString("pdtUnit"),
							obj.optInt("pdtisSet"), obj.getString("typeId"),
							obj.optInt("minrebate"), obj.optInt("notout"),
							obj.optInt("notshow"), obj.optInt("notshowonbill"),
							obj.optInt("pdtchgNumber"), Double.parseDouble(obj
									.optString("pdtdownprice1") + "0"),
							Double.parseDouble(obj.optString("pdtdownprice2")
									+ "0"));
					if (menu.getTypeId().equals("05")) {
					}
					menus.add(menu);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menus;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public List<d_MenuType> getMenuTypes() {
		List<d_MenuType> menuTypes = new ArrayList<d_MenuType>();
		d_MenuType menuType = new d_MenuType();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetMenuTypes";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("menuTypes");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);
					menuType = new d_MenuType(obj.getString("typeId"),
							obj.getString("typeName"),
							obj.getString("typeParentId"));
					menuTypes.add(menuType);
					// Log.e("Json",menuType.getTypeId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuTypes;
	}

	// 得到所有员工信息
	public List<d_Staff> getStaff() {
		List<d_Staff> staffs = new ArrayList<d_Staff>();
		d_Staff staff = new d_Staff();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetStaff";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			// Log.e("js",jsonObject+"");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("staffs");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);
					staff = new d_Staff(obj.getString("SAccount"),
							obj.getString("SPwd"), obj.getString("SName"),
							obj.getString("typeName"), obj.getInt("priority"));
					// Log.e("J",staff.getS_account());
					staffs.add(staff);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staffs;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public List<d_Area> getAreas() {
		List<d_Area> areas = new ArrayList<d_Area>();
		d_Area area = new d_Area();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetAreas";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Areas");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					area = new d_Area(obj.optString("areaId"),
							obj.optString("areaName"));
					areas.add(area);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return areas;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public List<d_Desk> getDesks(String area) {
		List<d_Desk> desks = new ArrayList<d_Desk>();
		d_Desk desk = new d_Desk();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetDesks";
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("area", area));

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Desks");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					desk = new d_Desk(obj.optInt("id"),// 桌子的编号
							obj.optString("typeId"), // 桌子所属区域
							obj.optString("state"), // 桌子的状态
							obj.optString("SAccount"), // 服务员账号
							obj.optString("deskName"), // 桌子的名字
							obj.optInt("statetime"), // 标记外卖状态
							obj.optString("starttime"), // 桌子开始的时间
							obj.optInt("peopleNum"), // 桌子坐的人数
							obj.optInt("row"), // 桌子所在的列
							obj.optInt("col"), // 桌子所在的行
							obj.optInt("delmark"), // 标记是否删除
							obj.optInt("message")); // 已做好菜单的数量
					desks.add(desk);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desks;
	}

	// //修改桌子上的信息
	public Boolean setDesks(d_Desk desk) {
		String url = BASEURL + "/Backoffice/SetDesk";
		Parameter para0 = new Parameter("state", desk.getState());
		Parameter para1 = new Parameter("s_account", desk.getS_account());// 服务员账号
		Parameter para2 = new Parameter("people_num", desk.getPeople_num() + "");// 桌子坐的人数
		Parameter para3 = new Parameter("id", desk.getDesk_name());// 桌子的名字
		Parameter para4 = new Parameter("message", desk.getMessage() + "");// 已做好菜单的数量
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	// 添加一张新桌子
	public Boolean addDesks(d_Desk desk) {
		String url = BASEURL + "/Backoffice/AddDesk";
		Parameter para0 = new Parameter("state", desk.getState());// 桌子的状态
		Parameter para1 = new Parameter("SAccount", desk.getS_account());// 服务员账号
		Parameter para2 = new Parameter("typeId", desk.getType_id()); // 桌子所属区域
		Parameter para3 = new Parameter("deskName", desk.getDesk_name());// 桌子名
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public Boolean setSaleAndPdtDone(d_SaleRecord current_sr) {

		String url = BASEURL + "/Backoffice/SetSaleAndPdtDone";
		Parameter para0 = new Parameter("salerecordId", current_sr.getItemNo());
		Parameter para1 = new Parameter("PdtCODE", current_sr.getPdtCODE());
		Parameter para2 = new Parameter("DeskName", current_sr.getDesk_name());
		Parameter para3 = new Parameter("closeTime", DateUtils.getDateEN());
		Parameter para4 = new Parameter("createTime",
				current_sr.getCreateTime());
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	/*
	 * 从后台得到厨师完成情况
	 */

	public List<d_Saleandpdt> getSaleandpdtDone(String deskName,
			String salerecordId) {
		List<d_Saleandpdt> salesDone = new ArrayList<d_Saleandpdt>();
		d_Saleandpdt saleDone = new d_Saleandpdt();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetSaleAndPdtDone?deskName="
				+ deskName + "&id=" + salerecordId;
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("srs");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					saleDone = new d_Saleandpdt(obj.getInt("id"), "",
							obj.optString("pdtCode"), obj.optString("pdtName"),
							obj.optInt("number"), 0, "", "", "",
							obj.optInt("priority"), obj.optString("status"),
							obj.optString("createTime"),
							obj.optString("closeTime"), 0);
					salesDone.add(saleDone);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesDone;
	}

	// public Boolean setSaleRecordFinish(d_SaleRecord sr) {
	// String url = BASEURL + "/Backoffice/SetSaleRecordFinish";
	// Parameter para0 = new Parameter("billid", sr.getBILLID());
	// Parameter para1 = new Parameter("pdtcode", sr.getPdtCODE());
	// Parameter para2 = new Parameter("closetime", DateUtils.getDateEN());
	// Parameter para3 = new Parameter("deskname", sr.getDesk_name());
	//
	// List<Parameter> paras = new ArrayList<Parameter>();
	// paras.add(para0);
	// paras.add(para1);
	// paras.add(para2);
	// paras.add(para3);
	//
	// SyncHttp syncHttp = new SyncHttp();
	// String json = null;
	// try {
	// json = syncHttp.httpPost(url, paras);
	// JSONObject jsonObject = new JSONObject(json);
	// return jsonObject.getString("ret").equals("success");
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// return false;
	// }
	// }
	public Boolean setSaleandpdtDelivered(d_Saleandpdt sr, String deskName) {
		String url = BASEURL + "/Backoffice/SetSaleAndPdtDelivered";
		Parameter para0 = new Parameter("id", sr.getId() + "");
		Parameter para1 = new Parameter("PdtCODE", sr.getPdtCode());
		Parameter para2 = new Parameter("closeTime", "");
		Parameter para3 = new Parameter("DeskName", deskName);
		Parameter para4 = new Parameter("salerecordId", sr.getSalerecordId());
		Parameter para5 = new Parameter("createTime", sr.getCreateTime());
		Log.i("tag", "salerecordId+createTime" + para4 + "" + para5);
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);
		paras.add(para5);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			Log.i("111111111111", jsonObject.toString());
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public Boolean setSaleandpdtFinish(d_SaleRecord sr) {
		String url = BASEURL + "/Backoffice/SetSaleAndPdtFinish";
		Parameter para0 = new Parameter("id", sr.getId() + "");
		Parameter para1 = new Parameter("PdtCODE", sr.getPdtCODE());
		Parameter para2 = new Parameter("closeTime", DateUtils.getDateEN());
		Parameter para3 = new Parameter("DeskName", sr.getDesk_name());
		Parameter para4 = new Parameter("salerecordId", sr.getItemNo());
		Parameter para5 = new Parameter("createTime", sr.getCreateTime());
		Log.i("tag", "salerecordId+createTime" + para4 + "" + para5);
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);
		paras.add(para5);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public Boolean setDrawerOpenTransaction(float initMoney, String drawerName) {
		String url = BASEURL + "/Backoffice/SetCashier?cashierId=" + drawerName
				+ "&initMoney=" + initMoney + "&createTime="
				+ DateUtils.getDateEN() + "&actual=" + initMoney
				+ "&different=" + 0 + "&controller=add";

		SyncHttp syncHttp = new SyncHttp();

		String json = null;
		try {
			Log.i("tag", "后台请求。。");
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");

		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

	}

	public boolean setDrawerOpen(String drawerName) {
		String url = BASEURL + "/Backoffice/SetCashierState";
		Parameter para0 = new Parameter("cashierId", drawerName);
		Parameter para1 = new Parameter("state", "1");
		Parameter para2 = new Parameter("controller", "update");
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public List<d_CashierInfor> getDrawerInfor(String cashier_id,
			String byTime, String time) {

		List<d_CashierInfor> cashierInfors = new ArrayList<d_CashierInfor>();
		d_CashierInfor cashierInfor = new d_CashierInfor();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetCashier?cashierId=" + cashier_id
				+ "&bytime=" + byTime + "&time=" + time;
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Cashier");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					cashierInfor = new d_CashierInfor(obj.getInt("id"),
							obj.optString("cashierId"),
							obj.optString("createTime"),
							obj.optString("closeTime"), Float.parseFloat(obj
									.optString("initMoney")),
							Float.parseFloat(obj.optString("different")),
							Float.parseFloat(obj.optString("actual")));
					cashierInfors.add(cashierInfor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cashierInfors;

	}

	public d_CashierInfor getCurrentDrawerInfor(String cashier_id) {
		d_CashierInfor infor = new d_CashierInfor();
		boolean ret = false;

		String url = BASEURL + "/Backoffice/GetCashier?cashierId=" + cashier_id
				+ "&bytime=time";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Cashier");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					infor = new d_CashierInfor(obj.getInt("id"),
							obj.optString("cashierId"),
							obj.optString("createTime"),
							obj.optString("closeTime"), Float.parseFloat(obj
									.optString("initMoney")),
							Float.parseFloat(obj.optString("different")),
							Float.parseFloat(obj.optString("actual")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return infor;
	}

	public float getCurrentDrawerSalesCash(String cashier_id, String createTime) {
		int totalMoney = 0;
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetBillByCashier?cashierId="
				+ cashier_id + "&createTime=" + createTime;
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Bills");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);
					totalMoney = obj.getInt("total");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return totalMoney;

	}

	public Boolean setDrawerStateCloseed(String drawerName, float actual_cash,
			float difference) {
		String url = BASEURL + "/Backoffice/SetCashier?cashierId=" + drawerName
				+ "&actual=" + actual_cash + "&different=" + difference
				+ "&controller=update" + "&closeTime=" + DateUtils.getDateEN();
		// Parameter para0 = new Parameter("cashierId", drawerName);
		// Parameter para1 = new Parameter("actual", actual_cash);
		// Parameter para2 = new Parameter("different", difference);
		// Parameter para3 = new Parameter("controller", "update");
		// Parameter para4 = new Parameter("closeTime", DateUtils.getDateEN());
		// List<Parameter> paras = new ArrayList<Parameter>();
		// paras.add(para0);
		// paras.add(para1);
		// paras.add(para2);
		// paras.add(para3);
		// paras.add(para4);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public Boolean setDrawerStateClose(String cashierId) {
		String url = BASEURL + "/Backoffice/SetCashierState?controller=update"
				+ "&cashierId=" + cashierId + "&state=0";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public Boolean setDrawerPayInOutTransaction(d_pay_in_out payInOut) {
		String url = BASEURL + "/Backoffice/SetCashierRecord?waiter="
				+ payInOut.getWaiter() + "&time=" + payInOut.getTime()
				+ "&description=" + payInOut.getDescription() + "&cashierId="
				+ payInOut.getCashierId() + "&money=" + payInOut.getMoney();
		
		Log.i("tag", url);

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public List<d_pay_in_out> getDrawerPayInOutTransaction1(String cashierId, 
			String time) {
		List<d_pay_in_out> pay_in_outs = new ArrayList<d_pay_in_out>();
		d_pay_in_out pay_in_out = new d_pay_in_out();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetCashierRecord?cashierId="
				+ cashierId + "&bytime=bytime" + "&time=" + time;
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("CashierRecord");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					pay_in_out = new d_pay_in_out(obj.getInt("id"), cashierId,
							obj.optString("waiter"), obj.optString("time"),
							Float.parseFloat(obj.optString("money")),
							obj.optString("description"));
					pay_in_outs.add(pay_in_out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pay_in_outs;
	}
	public List<d_pay_in_out> getDrawerPayInOutTransaction(String cashierId) {
		List<d_pay_in_out> pay_in_outs = new ArrayList<d_pay_in_out>();
		d_pay_in_out pay_in_out = new d_pay_in_out();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetCashierRecord?cashierId="
				+ cashierId + "&bytime=time";
		Log.i("tag",url);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("CashierRecord");
				JSONObject obj = null;
				for (int i = 0; i < objectArr.length(); i++) {
					obj = (JSONObject) objectArr.opt(i);

					pay_in_out = new d_pay_in_out(obj.getInt("id"), cashierId,
							obj.optString("waiter"), obj.optString("time"),
							Float.parseFloat(obj.optString("money")),
							obj.optString("description"));
					pay_in_outs.add(pay_in_out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pay_in_outs;
	}

	public Boolean LogOut(Context context, String s_account) {
		boolean ret = false;
		String url = BASEURL + "/Backoffice/Logout";
		String params = "s_account=" + s_account;
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				//
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean addUser(int i, String account, String password, String name) {

		String url = BASEURL + "/Backoffice/AddUser";
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(new Parameter("type", i + ""));
		paras.add(new Parameter("account", account));
		paras.add(new Parameter("password", password));
		paras.add(new Parameter("name", name));

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

	}

	public Boolean addContact(d_Contact contact) {

		String url = BASEURL + "/Backoffice/AddContact";
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(new Parameter("Name", contact.getName()));
		paras.add(new Parameter("Phone", contact.getPhone()));
		paras.add(new Parameter("Add_Number", contact.getAdd_Number()));
		paras.add(new Parameter("Add_Street", contact.getAdd_Street()));
		paras.add(new Parameter("Add_Apt", contact.getAdd_Apt()));
		paras.add(new Parameter("Add_City", contact.getAdd_City()));
		paras.add(new Parameter("Add_State", contact.getAdd_State()));
		paras.add(new Parameter("Add_Code", contact.getAdd_Code()));
		paras.add(new Parameter("Card_Number", contact.getCard_Number()));
		paras.add(new Parameter("Card_Date", contact.getCard_Date()));
		paras.add(new Parameter("Card_Cvv", contact.getCard_Cvv()));
		paras.add(new Parameter("Card_Fname", contact.getCard_Fname()));
		paras.add(new Parameter("Card_Lname", contact.getCard_Lname()));
		paras.add(new Parameter("Be_Notes", contact.getBe_Notes()));
		paras.add(new Parameter("Not_Notes", contact.getNot_Notes()));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			Log.i("tag", jsonObject.getString("ret"));
			return jsonObject.getString("ret").equals("success");

		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

	}

	public List<d_Customer> getBillAndPdt() {
		List<d_Customer> Customers = new ArrayList<d_Customer>();
		d_Customer Customer = new d_Customer();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetBillAndPdt";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("BillAndPdt");
				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);
					Customer = new d_Customer(rs.getString("billId"),
							rs.getInt("sapId"));
					Customers.add(Customer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Customers;
	}

	public Boolean setBillAndPdt(d_Customer billAndPdt) {
		String url = BASEURL + "/Backoffice/SetBillAndPdt";

		Parameter para0 = new Parameter("billId", billAndPdt.getCustomNo());
		Parameter para1 = new Parameter("sapId", String.valueOf(billAndPdt
				.getItemNo()));
		// Parameter para2 = new Parameter("subtotal", String.valueOf(localBill
		// .getSubtotal()));
		//
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		// paras.add(para2);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		// "billId="+billAndPdt.getCustomNo()+" & sapId="+String.valueOf(billAndPdt.getItemNo())
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			Log.i("tag",
					"sddd1" + billAndPdt.getCustomNo() + billAndPdt.getItemNo());
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.i("tag",
					"sddd0" + billAndPdt.getCustomNo() + billAndPdt.getItemNo());
			return false;
		}
	}

	public Boolean setBill(d_Bill localBill) {
		String url = BASEURL + "/Backoffice/SetBill";
		Parameter para0 = new Parameter("billId", localBill.getBillId());
		Parameter para1 = new Parameter("waiter", localBill.getWaiter());
		Parameter para2 = new Parameter("subtotal", String.valueOf(localBill
				.getSubtotal()));
		Parameter para3 = new Parameter("createTime", localBill.getCreateTime());
		Parameter para4 = new Parameter("distant", String.valueOf(localBill
				.getDistant()));
		Parameter para5 = new Parameter("tax", String.valueOf(localBill
				.getTaxTotal()));
		Parameter para6 = new Parameter("tip", String.valueOf(localBill
				.getTip()));
		Parameter para7 = new Parameter("total", String.valueOf(localBill
				.getTotal()));
		Parameter para8 = new Parameter("rebate", String.valueOf(localBill
				.getRebate()));
		Parameter para9 = new Parameter("salerecordId",
				localBill.getSalerecordId());
		Parameter para10 = new Parameter("tipPayment",
				localBill.getTipPayment());
		Parameter para11 = new Parameter("payment", localBill.getPayment());
		Parameter para12 = new Parameter("cashierId", String.valueOf(localBill
				.getCashierId()));
		Parameter para13 = new Parameter("initTotal", String.valueOf(localBill
				.getInitTotal()));
		Parameter para14 = new Parameter("dept", localBill.getDept());
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);
		paras.add(para5);
		paras.add(para6);
		paras.add(para7);
		paras.add(para8);
		paras.add(para9);
		paras.add(para10);
		paras.add(para11);
		paras.add(para12);
		paras.add(para13);
		paras.add(para14);

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public List<d_Bill> getBills() {
		List<d_Bill> bills = new ArrayList<d_Bill>();
		d_Bill bill = new d_Bill();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetBill";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Bills");
				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);
					bill = new d_Bill(rs.getString("billId"),
							rs.getString("salerecordId"),
							rs.getString("waiter"), Float.valueOf(rs
									.getString("subtotal")), Float.valueOf(rs
									.getString("taxTotal")), Float.valueOf(rs
									.getString("total")),
							rs.getString("createTime"), Float.valueOf(rs
									.getString("distant")), Float.valueOf(rs
									.getString("tip")), Float.valueOf(rs
									.getString("rebate")), Float.valueOf(rs
									.getString("initTotal")),
							rs.getString("tipPayment"),
							rs.getString("payment"), rs.getString("cashierId"),
							rs.getString("dept"));
					bills.add(bill);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bills;
	}

	// public List<d_Cashier> getCashier() {
	// List<d_Cashier> cashiers = new ArrayList<d_Cashier>();
	// d_Cashier cashier = new d_Cashier();
	// boolean ret = false;
	// String url = BASEURL + "/Backoffice/GetCashiers";
	// SyncHttp syncHttp = new SyncHttp();
	// String json = null;
	// try {
	// json = syncHttp.httpGet(url, null);
	// JSONObject jsonObject = new JSONObject(json);
	// // 获取返回码
	// ret = jsonObject.getString("ret").equals("success");
	// if (ret) {
	// JSONObject dataObject = jsonObject.getJSONObject("data");
	// // 获取返回用户集合
	// JSONArray objectArr = dataObject.getJSONArray("Bills");
	// JSONObject rs = null;
	// for (int i = 0; i < objectArr.length(); i++) {
	// rs = (JSONObject) objectArr.opt(i);
	//
	// cashier = new d_Cashier(rs.getInt("id"),
	// Float.valueOf("currentMoney"), Float.valueOf(rs
	// .getString("initMoney")), Float.valueOf(rs
	// .getString("changeMoney")),
	// rs.getString("createTime"),
	// rs.getString("userCode"),
	// rs.getString("cashierId"), rs.getString("status"),
	// rs.getString("otherspec"));
	// cashiers.add(cashier);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return cashiers;
	// }

	public Boolean setCashier(d_Cashier cashier) {
		String url = BASEURL + "/Backoffice/SetCashier";
		Parameter para0 = new Parameter("currentMoney",
				cashier.getCurrentMoney() + "");
		Parameter para1 = new Parameter("initMoney", cashier.getInitMoney()
				+ "");
		Parameter para2 = new Parameter("changeMoney", cashier.getChangeMoney()
				+ "");
		Parameter para3 = new Parameter("createTime", cashier.getCreateTime());
		Parameter para4 = new Parameter("userCode", cashier.getUserCode());
		Parameter para5 = new Parameter("cashierId", cashier.getCashierId());
		Parameter para6 = new Parameter("status", cashier.getStatus());

		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);
		paras.add(para5);
		paras.add(para6);

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public Boolean setCashierInitMoney(d_Cashier cashier) {
		String url = BASEURL + "/Backoffice/SetInitMoney";
		Parameter para0 = new Parameter("initmoney", cashier.getInitMoney()
				+ "");
		Parameter para1 = new Parameter("createtime", cashier.getCreateTime());
		Parameter para2 = new Parameter("usercode", cashier.getUserCode());
		Parameter para3 = new Parameter("cashierid", cashier.getCashierId());
		Parameter para4 = new Parameter("status", cashier.getStatus());

		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para0);
		paras.add(para1);
		paras.add(para2);
		paras.add(para3);
		paras.add(para4);

		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	// public List<d_Cashier> getCashiers(String cashierId) {
	// List<d_Cashier> cashiers = new ArrayList<d_Cashier>();
	// d_Cashier cashier = null;
	// boolean ret = false;
	// String url = BASEURL + "/Backoffice/GetCashiers";
	// List<Parameter> params = new ArrayList<Parameter>();
	// params.add(new Parameter("cashierId", cashierId));
	// SyncHttp syncHttp = new SyncHttp();
	// String json = null;
	// try {
	// json = syncHttp.httpPost(url, params);
	// JSONObject jsonObject = new JSONObject(json);
	// // 获取返回码
	// ret = jsonObject.getString("ret").equals("success");
	//
	// if (ret) {
	// Log.i("ret", ret + "");
	// JSONObject dataObject = jsonObject.getJSONObject("data");
	// // 获取返回用户集合
	// JSONArray objectArr = dataObject.getJSONArray("Cashiers");
	// JSONObject rs = null;
	// for (int i = 0; i < objectArr.length(); i++) {
	// rs = (JSONObject) objectArr.opt(i);
	// cashier = new d_Cashier(rs.getInt("id"), Float.valueOf(rs
	// .getString("currentMoney")), Float.valueOf(rs
	// .getString("initMoney")), Float.valueOf(rs
	// .getString("changeMoney")),
	// rs.getString("createTime"),
	// rs.getString("userCode"),
	// rs.getString("cashierId"), rs.getString("status"),
	// rs.getString("otherspec"));
	//
	// Log.i("rs.getIntId", rs.getInt("id") + "");
	// cashiers.add(cashier);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return cashiers;
	// }

	public d_Cashier getCashiers(String cashierId) {
		// List<d_Cashier> cashiers = new ArrayList<d_Cashier>();
		d_Cashier cashier = null;
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetCashierState?cashierId="
				+ cashierId;
		// List<Parameter> params = new ArrayList<Parameter>();
		// params.add(new Parameter("cashierId", cashierId));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			// Log.i("tag","前者。。。");
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			// Log.i("tag",ret+"qqqq");
			if (ret) {
				Log.i("ret", ret + "");
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Cashier");
				JSONObject rs = null;
				Log.i("tag", rs + "");
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);
					cashier = new d_Cashier(0, 0.0f, 0.0f, 0.0f, "", "",
							rs.getString("cashierId"), rs.getString("state"),
							"");

					// Log.i("rs.getIntId", rs.getInt("id") + "");
					// cashiers.add(cashier);
				}
			}
		} catch (Exception e) {
			Log.i("tag", "后者。。。");
			e.printStackTrace();
		}
		return cashier;
	}

	public Boolean checkUpadte() {
		boolean ret = false;
		String url = BASEURL + "/Backoffice/VersionUpdateServlet";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");
			Constant.versionCode = jsonObject.optInt("versionCode");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getTest() {
		String ret = "";
		String url = BASEURL + "/Backoffice/Test";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret");
			Log.i("tag", ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<d_Contact> getContacts() {
		List<d_Contact> contacts = new ArrayList<d_Contact>();
		d_Contact contact = new d_Contact();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetContact";
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpGet(url, null);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");

			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("contacts");
				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);
					contact = new d_Contact(rs.getString("name"),
							rs.getString("phone"), rs.getString("add_Number"),
							rs.getString("add_Street"),
							rs.getString("add_Apt"), rs.getString("add_City"),
							rs.getString("add_State"),
							rs.getString("add_Code"),
							rs.getString("card_Number"),
							rs.getString("card_Date"),
							rs.getString("card_Cvv"),
							rs.getString("card_Fname"),
							rs.getString("card_Lname"),
							rs.getString("be_Notes"), rs.getString("not_Notes"));
					contacts.add(contact);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}

	public Boolean deleteDeliverDesks(d_Desk desk) {

		String url = BASEURL + "/Backoffice/cleanDesk";
		Parameter para = new Parameter("deskName", desk.getDesk_name());
		List<Parameter> paras = new ArrayList<Parameter>();
		paras.add(para);
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, paras);
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("ret").equals("success");
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	// public Boolean setDrawer(String cashCount) {
	// String url = BASEURL + "/Backoffice/SetCashier";
	// Parameter para0 = new Parameter("startDrawerCashCount", cashCount);
	// List<Parameter> paras = new ArrayList<Parameter>();
	// paras.add(para0);
	// SyncHttp syncHttp = new SyncHttp();
	// String json = null;
	// try {
	// json = syncHttp.httpPost(url, paras);
	// JSONObject jsonObject = new JSONObject(json);
	// return jsonObject.getString("ret").equals("success");
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// return false;
	// }
	// }

	public List<d_Sale> getStaffSale(String waiterCount) {
		List<d_Sale> sales = new ArrayList<d_Sale>();
		d_Sale sale = new d_Sale();
		boolean ret = false;
		String url = BASEURL + "/Backoffice/GetStaffSale";
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("waiter", waiterCount));
		SyncHttp syncHttp = new SyncHttp();
		String json = null;
		try {
			json = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(json);
			// 获取返回码
			ret = jsonObject.getString("ret").equals("success");

			if (ret) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				// 获取返回用户集合
				JSONArray objectArr = dataObject.getJSONArray("Sales");
				JSONObject rs = null;
				for (int i = 0; i < objectArr.length(); i++) {
					rs = (JSONObject) objectArr.opt(i);

					sale = new d_Sale(rs.optString("itemNo"),
							rs.optString("closeTime"),
							rs.optString("createTime"),
							rs.optString("deskName"),
							rs.optString("otherSpec"),
							rs.optString("otherSpecNo1"),
							rs.optString("otherSpecNo2"),
							rs.optString("status"), rs.optString("dept"),
							(float) rs.optDouble("subTotal"),
							(float) rs.optDouble("tipTotal"),
							(float) rs.optDouble("total"),
							(float) rs.optDouble("initTotal"),
							(float) rs.optDouble("rebate"),
							(float) rs.optDouble("taxTotal"),
							rs.optString("waiter"),
							(float) rs.optDouble("cashTotal"),
							(float) rs.optDouble("cardTotal"),
							rs.optInt("customerId"));
					sales.add(sale);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sales;
	}
}
