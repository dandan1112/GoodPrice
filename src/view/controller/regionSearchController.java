package view.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import dao.ureaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import vo.GS_VO;

public class regionSearchController implements Initializable {

	private ObservableList<GS_VO> ov = FXCollections.observableArrayList();
	static Connection conn;
	ureaDAO dao = new ureaDAO();
	ResultSet rs;
	Statement stmt;

	@FXML
	private TextField regiontxt;
	@FXML
	private CheckBox lowFilter;
	@FXML
	private CheckBox inventFilter;
	@FXML
	private Button addFav;
	@FXML
	private Button regionSearchBtn;
	@FXML
	private Button ureaBtn;
	@FXML
	private Button refreshBtn;
	@FXML
	private Button favlistBtn;
	@FXML
	private CheckBox RIF;
	@FXML
	private CheckBox RPF;
	@FXML
	private CheckBox allF;
	@FXML
	private TableView<GS_VO> view;
	@FXML
	private TableColumn<GS_VO, String> name;
	@FXML
	private TableColumn<GS_VO, String> addr;
	@FXML
	private TableColumn<GS_VO, String> inventory;

	@FXML
	private TableColumn<GS_VO, String> price;
	@FXML
	private TableColumn<GS_VO, String> tel;
	@FXML
	private TableColumn<GS_VO, String> regDt;
	@FXML
	private TableColumn<GS_VO, String> code;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 각각의 버튼 실행 시 실행될 메서드 설정
		ureaBtn.setOnAction(e -> handleAllSearchBtn(e));
		regionSearchBtn.setOnAction(e -> handleRegionSearchBtn(e));
		favlistBtn.setOnAction(e -> handleFavListBtn(e));

		// 테이블에 데이터 없을 경우 출력 메시지
		view.setPlaceholder(new Label("데이터가 없습니다"));

		// 데이터-컬럼 간 매핑
		name.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("name"));
		addr.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("addr"));
		inventory.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("inventory"));
		price.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("price"));
		tel.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("tel"));
		regDt.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("regDt"));
		code.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("code"));

		try { // 컨트롤러 실행 시 즐겨찾기 테이블에 추가된 데이터를 초기화면으로 설정
			ov.clear();
			ov.addAll(dao.ASearch());
			view.setItems(ov);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 요소수 조회 창으로 이동 메서드 (버튼 클릭 시 실행)
		public void handleAllSearchBtn(ActionEvent event) {
			try {
				Parent pr = FXMLLoader.load(Class.forName("view.controller.allSearchController").getResource("/view/fxml/allSearchLayout.fxml"));
				StackPane root = (StackPane) ureaBtn.getScene().getRoot();
				root.getChildren().add(pr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 지역별 조회창으로 이동 메서드 (버튼 클릭 시 실행)
		public void handleRegionSearchBtn(ActionEvent event) {
			try {
				Parent pr = FXMLLoader.load(Class.forName("view.controller.regionSearchController").getResource("/view/fxml/regionSearchLayout.fxml"));
				StackPane root = (StackPane) regionSearchBtn.getScene().getRoot();
				root.getChildren().add(pr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 즐겨찾기 창으로 이동 메서드 (버튼 클릭 시 실행)
		public void handleFavListBtn(ActionEvent event) {
			try {
				Parent pr = FXMLLoader.load(Class.forName("view.controller.favoriteController").getResource("/view/fxml/favoriteLayout.fxml"));
				StackPane root = (StackPane) favlistBtn.getScene().getRoot();
				root.getChildren().add(pr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	// 지역별 데이터 조회 메서드 (빌터 클릭 시 실행)
	public void region(ActionEvent region) {
		Alert alert = new Alert(AlertType.ERROR);
		String addre = regiontxt.getText();
		List<GS_VO> list = dao.ASearch();

		try {
			ov.clear();
			ov.addAll(dao.RegionSearch(regiontxt.getText()));
			view.setItems(ov);
			if (regiontxt.getText().isEmpty()) {
				alert.setContentText("지역명을 입력하세요");
				alert.show();
			} else {
				view.setPlaceholder(new Label("올바른 지역명이 아닙니다!"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 지역별 낮은 가격순 정렬 메서드 (필터 클릭 시 실행)
	public void RP(ActionEvent region) {
		try {
			// 해당 필터 클릭 시 나머지 필터는 선택 해제하여 라디오 버튼과 같은 효과 적용
			RIF.setSelected(false);
			allF.setSelected(false);
			// 필터 클릭 시 RP필터가 체크되어있는 경우 지역별 낮은 가격순 데이터를 테이블에 세팅
			if (RPF.isSelected()) {
				ov.clear();
				ov.addAll(dao.RPFilter(regiontxt.getText()));
				view.setItems(ov);
			} else { // 필터 클릭 시 RP필터가 해제되는 경우 전체 데이터를 테이블에 세팅
				ov.clear();
				ov.addAll(dao.RegionSearch(regiontxt.getText()));
				view.setItems(ov);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 지역별 재고 있는 곳 정렬 메서드 (필터 클릭 시 실행)
	public void RI(ActionEvent region) {
		try {
			// 해당 필터 클릭 시 나머지 필터는 선택 해제하여 라디오 버튼과 같은 효과 적용
			RPF.setSelected(false);
			allF.setSelected(false);
			// 필터 클릭 시 RI필터가 체크되어있는 경우 지역별 재고 있는 곳 데이터를 테이블에 세팅
			if (RIF.isSelected()) {
				ov.clear();
				ov.addAll(dao.RIFilter(regiontxt.getText()));
				view.setItems(ov);
			} else { // 필터 클릭 시 RI필터가 해제되는 경우 전체 데이터를 테이블에 세팅
				ov.clear();
				ov.addAll(dao.RegionSearch(regiontxt.getText()));
				view.setItems(ov);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 지역별 낮은 가격 순 + 재고있는 곳 조회 메서드 (필터 클릭 시 실행)
	public void Rall(ActionEvent region) {
		try {
			// 해당 필터 클릭 시 나머지 필터는 선택 해제하여 라디오 버튼과 같은 효과 적용
			RIF.setSelected(false);
			RPF.setSelected(false);
			// 필터 클릭 시 Rall필터가 체크되어있는 경우 지역별 낮은 가격+재고 데이터를 테이블에 세팅
			if (allF.isSelected()) {
				ov.clear();
				ov.addAll(dao.threeFilter(regiontxt.getText()));
				view.setItems(ov);
			} else { // 필터 클릭 시 Rall필터가 해제되는 경우 전체 데이터를 테이블에 세팅
				ov.clear();
				ov.addAll(dao.RegionSearch(regiontxt.getText()));
				view.setItems(ov);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 새로고침 메서드 (버튼 클릭 시 실행)
	public void refresh() {
		try {
			dao.Refresh();

			Alert alert = new Alert(AlertType.INFORMATION); // 즐겨찾기 추가 시 확인 메시지 출력
			alert.setTitle(" ");
			alert.setHeaderText("새로고침이 완료되었습니다");
			alert.setContentText("Detail> OK버튼 클릭 시 안내창 종료");
			alert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 즐겨찾기 추가 메서드 (버튼 클릭 시 추가)
	public void add() {
		try {
			String addr = view.getSelectionModel().getSelectedItem().getAddr();
			String code = view.getSelectionModel().getSelectedItem().getCode();
			String inventory = view.getSelectionModel().getSelectedItem().getInventory();
			String name = view.getSelectionModel().getSelectedItem().getName();
			String price = view.getSelectionModel().getSelectedItem().getPrice();
			String tel = view.getSelectionModel().getSelectedItem().getTel();
			String regDt = view.getSelectionModel().getSelectedItem().getRegDt();
			if (code != null) {
				dao.addFav(addr, code, inventory, name, price, tel, regDt);
				
				Alert alert = new Alert(AlertType.INFORMATION); // 즐겨찾기 추가 시 확인 메시지 출력
				alert.setTitle(" ");
				alert.setHeaderText("즐겨찾기에 정상적으로 추가되었습니다");
				alert.setContentText("Detail> OK버튼 클릭 시 안내창 종료");
				alert.showAndWait();
			}
		} catch (NullPointerException e) {
			// 데이터를 선택하지 않고 실행 시 에러 메시지 출력
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(" ");
			alert.setHeaderText("즐겨찾을 주유소를 먼저 선택하세요");
			alert.setContentText("Detail> 주유소 선택 후 즐겨찾기 추가 버튼 누르기");
			alert.showAndWait();
		}
	}

}
