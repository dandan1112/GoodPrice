package db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import dao.ureaDAO;
import vo.GS_VO;

public class ureaDB {

	static PreparedStatement pstmt;
	static Statement stmt;
	static ResultSet rs;

	public static void main(String[] args) { // DB 파싱

		ureaDAO dao = new ureaDAO();

		dao.connDB();

		String key = "Bs%2FJ9blaKWTuHxoavKnzwfsh9pWHI8dLTh3wEFhUMcUwUb58%2Fx2HcPfntX%2Fvbn0nd18Xy0YeqY75yJ2lsntQtQ%3D%3D";
		String result = "";

		try {
			URL url = new URL("https://api.odcloud.kr/api/uws/v1/inventory?page=1&perPage=1000&serviceKey=" + key);
			BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			result = bf.readLine();
			JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
			JSONArray data = (JSONArray) jsonObject.get("data");
			List<GS_VO> gslist = new ArrayList<>();
			int count = 0;
			pstmt = dao.conn.prepareStatement("INSERT INTO urea_table VALUES(?,?,?,?,?,?,?)");

			for (int i = 0; i < data.size(); i++) {
				JSONObject tmp = (JSONObject) data.get(i);
				GS_VO gvo = new GS_VO();
				gvo.setAddr((String) tmp.get("addr"));
				gvo.setCode((String) tmp.get("code"));
				gvo.setInventory((String) tmp.get("inventory"));
				gvo.setName((String) tmp.get("name"));
				gvo.setPrice((String) tmp.get("price"));
				gvo.setTel((String) tmp.get("tel"));
				gvo.setRegDt((String) tmp.get("regDt"));

				gslist.add(gvo);
			}

			for (int i = 0; i < data.size(); i++) {
				pstmt.setString(1, gslist.get(i).getAddr());
				pstmt.setString(2, gslist.get(i).getCode());
				pstmt.setString(3, gslist.get(i).getInventory());
				pstmt.setString(4, gslist.get(i).getName());
				pstmt.setString(5, gslist.get(i).getPrice());
				pstmt.setString(6, gslist.get(i).getTel());
				pstmt.setString(7, gslist.get(i).getRegDt());

				pstmt.addBatch();
				pstmt.clearParameters();

				count++;
			} // for 끝

			pstmt.executeBatch();
			pstmt.clearBatch();

			System.out.println(count);
			System.out.println("추가 확인");

			pstmt.close();
			dao.conn.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}