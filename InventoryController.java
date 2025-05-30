import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert; // Import Alert

public class InventoryController implements Initializable {

    @FXML
    private TableView<Item> tableView;

    @FXML
    private TableColumn<Item, String> albumColumn;

    @FXML
    private TableColumn<Item, String> artistColumn;

    @FXML
    private TableColumn<Item, Integer> availableColumn;

    @FXML
    private TableColumn<Item, Integer> totalColumn;

    @FXML
    private TextField textFieldAlbum;

    @FXML
    private TextField textFieldArtist;

    @FXML
    private TextField textFieldJumlah;

    @FXML
    private TextField textFieldRented;

    @FXML
    private Button rentButton;

    @FXML
    private Button hapusButton;

    @FXML
    private Button tambahButton;

    @FXML
    private Button updateButton;

    public class Item {
        private String album;
        private String artist;
        private int available;
        private int total;

        public Item(String album, String artist, int available, int total) {
            this.album = album;
            this.artist = artist;
            this.available = available;
            this.total = total;
        }

        // Getters
        public String getAlbum() {
            return album;
        }

        public String getArtist() {
            return artist;
        }

        public int getAvailable() {
            return available;
        }

        public int getTotal() {
            return total;
        }

        // Setters
        public void setAlbum(String album) {
            this.album = album;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public void setAvailable(int available) {
            this.available = available;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    // Observable list untuk menyimpan data item
    private ObservableList<Item> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set cell value factories untuk tabel
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));


        // Set item ke table
        tableView.setItems(items);

       // Kemudian di dalam metode initialize
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {
        @Override
                public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
                    if (newValue != null) {
            displayItemDetails(newValue);
                        }
                     }
                });
    }

    // Method untuk menampilkan detail item pada textfields
    private void displayItemDetails(Item item) {
        textFieldAlbum.setText(item.getAlbum());
        textFieldArtist.setText(item.getArtist());
        textFieldJumlah.setText(String.valueOf(item.getTotal()));
        textFieldRented.setText(String.valueOf(item.getTotal() - item.getAvailable()));
    }

    // Method untuk menangani aksi tombol rent
    @FXML
    void rentButton(ActionEvent event) {
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.getAvailable() > 0) {
            selectedItem.setAvailable(selectedItem.getAvailable() - 1);
            tableView.refresh();
            displayItemDetails(selectedItem);
            showAlert("Sukses", "Album berhasil disewakan!", Alert.AlertType.INFORMATION); // Alert untuk rent
        } else if (selectedItem != null && selectedItem.getAvailable() == 0) {
            showAlert("Perhatian", "Album ini sudah tidak tersedia untuk disewakan!", Alert.AlertType.WARNING); // Alert jika tidak tersedia
        } else {
            showAlert("Error", "Tidak ada item yang dipilih untuk disewakan!", Alert.AlertType.ERROR);
        }
    }

    // Method untuk menangani aksi tombol hapus
    @FXML
    void hapusButton(ActionEvent event) {
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            items.remove(selectedItem);
            clearFields();
            showAlert("Sukses", "Data album berhasil dihapus!", Alert.AlertType.INFORMATION); // Alert sukses hapus
        } else {
            showAlert("Error", "Tidak ada item yang dipilih untuk dihapus!", Alert.AlertType.ERROR); // Alert jika tidak ada item terpilih
        }
    }

    // Method untuk menangani aksi tombol tambah
    @FXML
    void tambahButton(ActionEvent event) {
        try {
            String album = textFieldAlbum.getText();
            String artist = textFieldArtist.getText();
            int total = Integer.parseInt(textFieldJumlah.getText());
            int rented = Integer.parseInt(textFieldRented.getText());

            if (album.isEmpty() || artist.isEmpty()) {
                showAlert("Error", "Album dan artist tidak boleh kosong!", Alert.AlertType.ERROR); // Alert validasi
                return;
            }

            if (total < rented) {
                showAlert("Error", "Jumlah total tidak boleh lebih kecil dari jumlah rented!", Alert.AlertType.ERROR); // Alert validasi
                return;
            }

            int available = total - rented;
            Item newItem = new Item(album, artist, available, total);
            items.add(newItem);
            clearFields();
            showAlert("Sukses", "Data album berhasil ditambahkan!", Alert.AlertType.INFORMATION); // Alert sukses tambah

        } catch (NumberFormatException e) {
            showAlert("Error", "Jumlah total dan rented harus berupa angka!", Alert.AlertType.ERROR); // Alert exception
        }
    }

    // Method untuk menangani aksi tombol update
    @FXML
    void updateButton(ActionEvent event) {
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                String album = textFieldAlbum.getText();
                String artist = textFieldArtist.getText();
                int total = Integer.parseInt(textFieldJumlah.getText());
                int rented = Integer.parseInt(textFieldRented.getText());

                if (album.isEmpty() || artist.isEmpty()) {
                    showAlert("Error", "Album dan artist tidak boleh kosong!", Alert.AlertType.ERROR); // Alert validasi
                    return;
                }

                if (total < rented) {
                    showAlert("Error", "Jumlah total tidak boleh lebih kecil dari jumlah rented!", Alert.AlertType.ERROR); // Alert validasi
                    return;
                }

                int available = total - rented;
                selectedItem.setAlbum(album);
                selectedItem.setArtist(artist);
                selectedItem.setTotal(total);
                selectedItem.setAvailable(available);

                tableView.refresh();
                showAlert("Sukses", "Data album berhasil diubah!", Alert.AlertType.INFORMATION); // Alert sukses ubah
            } catch (NumberFormatException e) {
                showAlert("Error", "Jumlah total dan rented harus berupa angka!", Alert.AlertType.ERROR); // Alert exception
            }
        } else {
            showAlert("Error", "Tidak ada item yang dipilih!", Alert.AlertType.ERROR); // Alert jika tidak ada item terpilih
        }
    }

    // Method untuk membersihkan fields
    private void clearFields() {
        textFieldAlbum.clear();
        textFieldArtist.clear();
        textFieldJumlah.clear();
        textFieldRented.clear();
        tableView.getSelectionModel().clearSelection();
    }

    // Method untuk menampilkan alert (diperbarui)
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}