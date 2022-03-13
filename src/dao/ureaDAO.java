package dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import vo.GS_VO;

public class ureaDAO {

	static String driver = "oracle.jdbc.driver.OracleDriver";
	static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	static String user = "scott";
	static String password = "tiger";

	public static Connection conn;
	static PreparedStatement pstmt;
	static Statement stmt;
	static ResultSet rs;

	List<GS_VO> list = new ArrayList();
	GS_VO vo = new GS_VO();

	// 드라이버 연결 메서드
	public static void connDB() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 전체 조회 메서드
	public List<GS_VO> ASearch() {
		try {
			connDB();
			list.clear();

			stmt = conn.createStatement();
			String query = "select * from urea_table where price not in ('undefined', 'null')"; // 가격 데이터 중 undefined 데이터와 null값 제외
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("Asearch 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// 재고 있는 곳 검색 메서드
	public List<GS_VO> InvenSearch() {
		try {
			connDB();
			list.clear();

			stmt = conn.createStatement();
			String query = "SELECT * FROM urea_table WHERE inventory > 0 and price not in ('null', 'undefined')"; // 재고량이 0보다 큰 데이터만 가져옴
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("InvenSearch 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// 낮은 가격 순 검색 메서드
	public List<GS_VO> LpriceSearch() {
		try {
			connDB();
			list.clear();

			stmt = conn.createStatement();

			String query = "select * from urea_table where price not in('null','0','undefined') order by price *4 asc";
			// 모든 데이터가 String 타입으로 되어있어 일반적인 오름차순 정렬이 어려움
			// 'price ~ asc'를 통해 오름차순 정렬하면 낮은 숫자이면서 자릿수가 작은 데이터가 먼저 나옴.
			// X00원대 가격 중 현실적으로 400원대 데이터가 파싱 데이터 중 가장 낮아 앞자리가 '4'인 데이터를 기준으로 정렬되도록 함

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);

			}
			System.out.println("Lprice 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// 재고있는 곳 검색 + 낮은 가격 순 검색을 동시에 하는 메서드
	public List<GS_VO> twoFilter() {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();
			String query = "select * from urea_table where inventory > 0"
					+ " and price not in ('null','undefined','0') order by price *4 asc, inventory *4 asc";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("twoFilter 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 새로 고침 메서드
	public List<GS_VO> Refresh() {
		try {
			connDB();
			list.clear();

			stmt = conn.createStatement();
			String query = "drop table urea_table";
			rs = stmt.executeQuery(query);

			String query2 = "create table urea_table(addr varchar2(100), code varchar2(100), inventory varchar2(100), name varchar2(100), price varchar2(100), tel varchar2(100), regDt varchar2(100))";
			rs = stmt.executeQuery(query2);

			try {

				String key = "Bs%2FJ9blaKWTuHxoavKnzwfsh9pWHI8dLTh3wEFhUMcUwUb58%2Fx2HcPfntX%2Fvbn0nd18Xy0YeqY75yJ2lsntQtQ%3D%3D";
				String result = "";
				URL url = new URL("https://api.odcloud.kr/api/uws/v1/inventory?page=1&perPage=1000&serviceKey=" + key);
				BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
				result = bf.readLine();
				JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
				JSONArray data = (JSONArray) jsonObject.get("data");
				List<GS_VO> list = new ArrayList<>();
				pstmt = conn.prepareStatement("INSERT INTO urea_table VALUES(?,?,?,?,?,?,?)");

				for (int i = 0; i < data.size(); i++) {
					JSONObject tmp = (JSONObject) data.get(i);
					GS_VO vo = new GS_VO();
					vo.setAddr((String) tmp.get("addr"));
					vo.setCode((String) tmp.get("code"));
					vo.setInventory((String) tmp.get("inventory"));
					vo.setName((String) tmp.get("name"));
					vo.setPrice((String) tmp.get("price"));
					vo.setTel((String) tmp.get("tel"));
					vo.setRegDt((String) tmp.get("regDt"));

					list.add(vo);
				}

				for (int i = 0; i < data.size(); i++) {
					pstmt.setString(1, list.get(i).getAddr());
					pstmt.setString(2, list.get(i).getCode());
					pstmt.setString(3, list.get(i).getInventory());
					pstmt.setString(4, list.get(i).getName());
					pstmt.setString(5, list.get(i).getPrice());
					pstmt.setString(6, list.get(i).getTel());
					pstmt.setString(7, list.get(i).getRegDt());
					pstmt.addBatch();
					pstmt.clearParameters();
				}
				System.out.println("Refresh 실행");

				pstmt.executeBatch();
				pstmt.clearBatch();

			} catch (Exception e) {
				e.printStackTrace();
			}
			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 특정 지역의 데이터 검색 메서드
	public List<GS_VO> RegionSearch(String region) {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();

			rs = stmt.executeQuery("select * from urea_table where substr(addr,0,8) like '%" + region
					+ "%' and price not in('undefined','null')");
			// 전체 주소에서 검색 시 뒤의 글자와 붙어 지역명이 아님에도 검색 결과에 포함되는 경우가 있었음 ex) 북부산업로 -> '부산'으로 검색시 결과에 포함
			// 주소명의 앞에서 8자리까지 안에 모든 데이터가 나와있어 8자리까지 글자를 잘라 그 안에서만 데이터 비교
			
			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("RegionSearch 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// 특정 지역의 데이터 중 재고가 있는 곳 조회 메서드
	public List<GS_VO> RIFilter(String region) {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();
			String query = "select * from urea_table" + " where inventory > 0" + " and substr(addr,0,8) like '%"
					+ region + "%'";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("RIFilter 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 특정 지역의 데이터 중 낮은 가격 순 조회 메서드
	public List<GS_VO> RPFilter(String region) {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();
			String query = "select * from urea_table" + " where price not in ('null','undefined','0') "
					+ " and substr(addr,0,8) like '%" + region + "%'order by price *4 asc, inventory *4 asc";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("RPFilter 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 특정 지역 데이터 검색 + 재고 있는 곳 검색 + 낮은 가격 순 검색을 동시에 하는 메서드
	public List<GS_VO> threeFilter(String region) {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();
			String query = "select * from urea_table where inventory > 0"
					+ " and price not in ('null','undefined','0') " + " and substr(addr,0,8) like '%" + region
					+ "%' order by price *4 asc, inventory *4 asc";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String name = rs.getString("name");
				String inventory = rs.getString("inventory");
				String price = rs.getString("price");
				String addr = rs.getString("addr");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");
				String code = rs.getString("code");

				GS_VO vo = new GS_VO();
				vo.setName(name);
				vo.setInventory(inventory);
				vo.setPrice(price);
				vo.setAddr(addr);
				vo.setTel(tel);
				vo.setRegDt(regDt);
				vo.setCode(code);

				list.add(vo);
			}
			System.out.println("threeFilter 실행");

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 즐겨찾기 리스트 조회 메서드
	public List<GS_VO> FSearch() {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();
			String query = "select * from favorite_table";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String addr = rs.getString("addr");
				String code = rs.getString("code");
				String inventory = rs.getString("inventory");
				String name = rs.getString("name");
				String price = rs.getString("price");
				String tel = rs.getString("tel");
				String regDt = rs.getString("regDt");

				GS_VO vo = new GS_VO();
				vo.setAddr(addr);
				vo.setCode(code);
				vo.setInventory(inventory);
				vo.setName(name);
				vo.setPrice(price);
				vo.setTel(tel);
				vo.setRegDt(regDt);

				list.add(vo);
			}
			System.out.println("Fsearch 실행");

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// 즐겨찾기 리스트에 데이터 추가 메서드
	public void addFav(String addr, String code, String inventory, String name, String price, String tel,
			String regDt) {
		try {
			connDB();
			stmt = conn.createStatement();
			stmt.executeUpdate("insert into favorite_table values ('" + addr + "', '" + code + "', '" + inventory
					+ "', '" + name + "', '" + price + "', '" + tel + "', '" + regDt + "')");

			// 테이블에서 선택된 행의 모든 데이터를 가져와 즐겨찾기 테이블에 추가
			for (int i = 0; i < 100; i++) {
				JSONObject tmp = null;

				GS_VO vo = new GS_VO();
				vo.setAddr((String) tmp.get("addr"));
				vo.setCode((String) tmp.get("code"));
				vo.setInventory((String) tmp.get("inventory"));
				vo.setName((String) tmp.get("name"));
				vo.setPrice((String) tmp.get("price"));
				vo.setTel((String) tmp.get("tel"));
				vo.setRegDt((String) tmp.get("regDt"));
			}

		} catch (Exception e) {
			System.out.println("즐겨찾기 추가 완료");
		}

	}

	// 즐겨찾기 리스트에 추가된 데이터 삭제 메서드
	public List<GS_VO> delFav(String selected) {
		try {
			connDB();
			list.clear();
			stmt = conn.createStatement();

			String query = ("delete from favorite_table where code='" + selected + "'");
			// 데이터 중 고유한 값을 가지고 있는 'code(주유소 코드)'컬럼의  값을 이용해  데이터 삭제
			stmt.executeUpdate(query);
			System.out.println("즐겨찾기 삭제 완료");
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

}