
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    /* ──────────────────────────
       1.  TABLE MODEL
       ────────────────────────── */
    public static class Student {
        private final Integer id;
        private final String first;
        private final String last;
        private final String dept;
        private final String major;
        private final String email;
        private final String imageUrl;

        public Student(Integer id, String first, String last,
                       String dept, String major,
                       String email, String imageUrl) {
            this.id = id;
            this.first = first;
            this.last = last;
            this.dept = dept;
            this.major = major;
            this.email = email;
            this.imageUrl = imageUrl;
        }
        public Integer getId()      { return id;      }
        public String  getFirst()   { return first;   }
        public String  getLast()    { return last;    }
        public String  getDept()    { return dept;    }
        public String  getMajor()   { return major;   }
        public String  getEmail()   { return email;   }
        public String  getImageUrl(){ return imageUrl;}
    }

    /* ──────────────────────────
       2.  UI CONTROLS
       ────────────────────────── */
    private final TextField idFld    = new TextField();
    private final TextField firstFld = new TextField("First Name");
    private final TextField lastFld  = new TextField("Last Name");
    private final TextField deptFld  = new TextField("Department");
    private final TextField majorFld = new TextField("Major");
    private final TextField emailFld = new TextField("Email");
    private final TextField imageFld = new TextField("imageURL");

    private final Button clearBtn = new Button("Clear");
    private final Button addBtn   = new Button("Add");
    private final Button delBtn   = new Button("Delete");
    private final Button editBtn  = new Button("Edit");

    private TableView<Student> table;

    /* ──────────────────────────
       3.  START
       ────────────────────────── */
    @Override
    public void start(Stage stage) {
        MenuBar  menuBar   = buildMenuBar();
        VBox     leftPane  = buildProfilePane();
        table              = buildTable();
        VBox     rightPane = buildFormPane();

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(leftPane);
        root.setCenter(table);
        root.setRight(rightPane);
        root.setBottom(buildFooter());

        Scene scene = new Scene(root, 1000, 650);
        URL css = getClass().getResource("style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println("❌ style.css not found!");
        }


        stage.setTitle("FSC CSC325 – Full Stack Project");
        stage.setScene(scene);
        stage.show();

        wireActions();
    }

    /* ──────────────────────────
       4.  BUILDERS
       ────────────────────────── */
    private MenuBar buildMenuBar() {
        Menu file  = new Menu("File");
        Menu edit  = new Menu("Edit");
        Menu theme = new Menu("Theme");
        Menu help  = new Menu("Help");
        file.getItems().addAll(new MenuItem("New"), new MenuItem("Exit"));
        return new MenuBar(file, edit, theme, help);
    }

    private VBox buildProfilePane() {
        ImageView avatar = new ImageView(
                Objects.requireNonNull(getClass().getResource("/avatar.png"))
                        .toExternalForm()
        );
        avatar.setFitWidth(100);
        avatar.setFitHeight(100);

        VBox box = new VBox(avatar);
        box.setPadding(new Insets(15, 10, 10, 10));
        box.setAlignment(Pos.TOP_CENTER);
        box.getStyleClass().add("side-pane");
        return box;
    }

    private TableView<Student> buildTable() {
        TableView<Student> tv = new TableView<>();

        TableColumn<Student, Integer> idCol    = new TableColumn<>("ID");
        TableColumn<Student, String>  fCol     = new TableColumn<>("First Name");
        TableColumn<Student, String>  lCol     = new TableColumn<>("Last Name");
        TableColumn<Student, String>  deptCol  = new TableColumn<>("Department");
        TableColumn<Student, String>  majorCol = new TableColumn<>("Major");
        TableColumn<Student, String>  emailCol = new TableColumn<>("Email");

        idCol.setCellValueFactory   (new PropertyValueFactory<>("id"));
        fCol.setCellValueFactory    (new PropertyValueFactory<>("first"));
        lCol.setCellValueFactory    (new PropertyValueFactory<>("last"));
        deptCol.setCellValueFactory (new PropertyValueFactory<>("dept"));
        majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        tv.getColumns().addAll(idCol, fCol, lCol, deptCol, majorCol, emailCol);
        tv.setPlaceholder(new Label("No content in table"));
        return tv;
    }

    private VBox buildFormPane() {
        VBox box = new VBox(
                8,  // spacing (double)
                idFld, firstFld, lastFld,
                deptFld, majorFld, emailFld, imageFld,
                clearBtn, addBtn, delBtn, editBtn
        );
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.TOP_CENTER);
        box.getStyleClass().add("form-pane");
        return box;
    }

    private Region buildFooter() {
        Region footer = new Region();
        footer.setPrefHeight(25);
        footer.getStyleClass().add("footer");
        return footer;
    }

    /* ──────────────────────────
       5.  ACTION HANDLERS
       ────────────────────────── */
    private void wireActions() {
        clearBtn.setOnAction(e -> {
            idFld.clear(); firstFld.clear(); lastFld.clear();
            deptFld.clear(); majorFld.clear(); emailFld.clear(); imageFld.clear();
        });

        addBtn.setOnAction(e -> {
            Student s = new Student(
                    parseIntSafe(idFld.getText()),
                    firstFld.getText(), lastFld.getText(),
                    deptFld.getText(), majorFld.getText(),
                    emailFld.getText(), imageFld.getText()
            );
            table.getItems().add(s);
            clearBtn.fire();
        });

        delBtn.setOnAction(e -> {
            Student sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) table.getItems().remove(sel);
        });

        editBtn.setOnAction(e -> {
            Student sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            Student upd = new Student(
                    parseIntSafe(idFld.getText()),
                    firstFld.getText(), lastFld.getText(),
                    deptFld.getText(), majorFld.getText(),
                    emailFld.getText(), imageFld.getText()
            );
            int idx = table.getItems().indexOf(sel);
            table.getItems().set(idx, upd);
            clearBtn.fire();
        });
    }

    private int parseIntSafe(String txt) {
        try { return Integer.parseInt(txt.trim()); }
        catch (NumberFormatException ex) { return 0; }
    }

    /* ──────────────────────────
       6.  MAIN
       ────────────────────────── */
    public static void main(String[] args) {
        launch();
    }
}
