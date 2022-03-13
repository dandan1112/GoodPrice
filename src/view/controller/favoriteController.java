package view.controller;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import vo.GS_VO;

public class favoriteController implements Initializable {

	private ObservableList<GS_VO> ov = FXCollections.observableArrayList();
	static Connection conn;
	ureaDAO dao = new ureaDAO();

	@FXML
	private Button deleteBtn;
	@FXML
	private Button regionSearchBtn;
	@FXML
	private Button ureaBtn;
	@FXML
	private Button refreshBtn;
	@FXML
	private Button favlistBtn;
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

		// 즐겨찾기 테이블에 추가된 데이터가 없을 경우 출력 메시지 
		view.setPlaceholder(new Label("즐겨 찾는 주유소가 없습니다"));

		name.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("name"));
		addr.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("addr"));
		inventory.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("inventory"));
		price.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("price"));
		tel.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("tel"));
		regDt.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("regDt"));
		code.setCellValueFactory(new PropertyValueFactory<GS_VO, String>("code"));

		try { // 컨트롤러 실행 시 즐겨찾기 테이블에 추가된 데이터를 초기화면으로 설정
			ov.clear();
			ov.addAll(dao.FSearch());
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

	// 새로고침 메서드 (버튼 클릭 시 실행)
	public void refresh() {
		try {
			dao.Refresh();

			Alert alert = new Alert(AlertType.INFORMATION); // 즐겨찾기 추가 시 확인 메시지 출력
			alert.setTitle(" ");
			alert.setHeaderText("새로고침이 완료되었습니다");
			alert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 즐겨찾기 리스트에서 선택한 데이터 삭제 메서드 (버튼 클릭 시 실행)
	public void del() {
		try {
			String selected = view.getSelectionModel().getSelectedItem().getCode();
			if (selected != null) {
				ov.clear();
				dao.delFav(selected);
				ov.addAll(dao.FSearch());
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // 삭제할 데이터를 선택하지 않고 삭제 버튼 누를 시 에러 메시지 출력
			alert.setTitle(" ");
			alert.setHeaderText("삭제할 주유소를 먼저 선택하세요");
			alert.setContentText("Detail> 주유소 선택 후 삭제 버튼 누르기");
			alert.showAndWait();
		}
	}
}
