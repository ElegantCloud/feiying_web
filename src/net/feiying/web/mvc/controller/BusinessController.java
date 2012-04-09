package net.feiying.web.mvc.controller;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.feiying.db.DBHelper;

@Controller
@RequestMapping("/business")
public class BusinessController {

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/opennotify")
	public void changeBusinessStatus(
			@RequestParam(value = "username", required = true) String username) {
		// notify that user has committed business open application
		try {
			String sql = "SELECT business_status FROM fy_user WHERE username = ?";
			String status = DBHelper.getInstance().scalar(sql,
					new Object[] { username });
			if (status != null && !status.equals("opened")) {
				sql = "UPDATE fy_user SET business_status = 'processing' WHERE username = ?";
				DBHelper.getInstance().update(sql, new Object[] { username });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/getstatus")
	@Deprecated
	public @ResponseBody
	String getBusinessStatus(
			@RequestParam(value = "username", required = true) String username) {
		String sql = "SELECT business_status AS status FROM fy_user WHERE username=?";
		JSONObject ret = new JSONObject();
		try {
			String status = DBHelper.getInstance().scalar(sql,
					new Object[] { username });
			ret.put("status", status);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return ret.toString();
	}
}
