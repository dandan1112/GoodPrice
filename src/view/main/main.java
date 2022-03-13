package view.main;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class main extends Application {

	@FXML
	private Button btn;

	@Override
	public void start(Stage stage) throws Exception {
		// 프로그램 실행 시 main.fxml로 이동
		Parent root = FXMLLoader.load(Class.forName("view.main.main").getResource("/view/fxml/main.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

		System.setProperty("prism.lcdtext", "false"); // 안티 엘리어싱
		// 메인에서 쓸 폰트 로드
		Font.loadFont(getClass().getResourceAsStream("/resources/BlackHanSans-Regular.ttf"), 10);

		// 메인에서 쓸 styleSheet 설정
		scene.getStylesheets().add(getClass().getResource("/view/css/application.css").toExternalForm());
		// 메인에서 쓸 이미지 설정
		stage.getIcons().add(new Image("file:C:\\Dev\\workspace_java\\GoodPrice\\images\\ureawater.png"));
		
		stage.setOnCloseRequest(event -> { event.consume();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("프로그램 종료");
		alert.setHeaderText("프로그램을 종료하시겠습니까?");
		alert.setContentText("OK 버튼 클릭 시 프로그램이 종료됩니다.");
		Optional<ButtonType> option = alert.showAndWait();
		
		if (option.get().equals(ButtonType.OK)) {
		stage.close(); System.exit(1);
		}
		});
	}

	// 다음 화면으로 이동 메서드 (버튼 누르면 이동)
	public void handleAllBtn(ActionEvent event) {
		try {

			System.setProperty("prism.lcdtext", "false"); // 안티 앨리어싱

			// 다음 화면들에서 쓸 폰트 로드
			Font.loadFont(getClass().getResourceAsStream("/resources/BMHANNAPro.ttf"), 10);
			Font.loadFont(getClass().getResourceAsStream("/resources/BMHANNAAir_ttf.ttf"), 10);

			// 폰트 이름 확인 코드
//			String font2 = Font.loadFont(getClass().getResourceAsStream("/resources/BlackHanSans-Regular.ttf"), 10).getFamily();
//	        System.out.println(font2);

			Parent pr = FXMLLoader.load(getClass().getResource("/view/fxml/allSearchLayout.fxml")); // 버튼 누르면 이동할 다음 창 설정
			Scene scene = new Scene(pr);
			Stage stage = (Stage) btn.getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("Good Price");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}