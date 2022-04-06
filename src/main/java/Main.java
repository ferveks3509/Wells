import model.Equipment;
import model.Well;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import repository.Repository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public void init(Scanner scanner, Repository repository) throws IOException, TransformerException, ParserConfigurationException, SQLException {
        boolean run = true;
        while (run) {
            showMenu();
            System.out.println("Выбор меню: ");
            int select = Integer.parseInt(scanner.nextLine());
            if (select == 1) {
                System.out.println("Введите кол-во оборудования: ");
                int value = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите имя скважины: ");
                String name = scanner.nextLine();
                Well well = findWellOrCreate(name, repository);
                while (value != 0) {
                    repository.createEquipment(new Equipment(nameEquipment(repository), well.getId()));
                    value--;
                }
            } else if (select == 2) {
                System.out.println("Укажите 3 имени скважин через пробел:");
                String input = scanner.nextLine();
                String[] namesWells = input.split(" ");
                repository.init();
                repository.findAllWellsAndCountEquipments(namesWells).forEach(System.out::println);
            } else if (select == 3) {
                System.out.println("Укажите имя файла");
                String name = scanner.nextLine();
                printDom(name, repository);
            } else if (select == 4) {
                run = false;
            }
        }
    }

    private void printDom(String name, Repository repository) throws ParserConfigurationException, TransformerException, IOException, SQLException {
        repository.init();
        List<Well> wells = repository.findAllWells();
        List<Equipment> eqs = repository.findAllEquipments();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("dbinfo");
        document.appendChild(root);

        for (Well el : wells) {
            Element well = document.createElement("well");
            root.appendChild(well);

            well.setAttribute("name", el.getName());
            well.setAttribute("id", String.valueOf(el.getId()));

            for (Equipment eq : eqs) {
                if (el.getId() == eq.getWell_id()) {
                    Element equipment = document.createElement("equipment");
                    well.appendChild(equipment);

                    equipment.setAttribute("name", eq.getName());
                    equipment.setAttribute("id", String.valueOf(eq.getId()));
                }
            }
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("./" + name + ".xml")));
    }

    private String nameEquipment(Repository repository) throws SQLException {
        int count = repository.findMinId().getId();
        String name = "EQ" + count;
        return name;
    }

    private Well findWellOrCreate(String name, Repository repository) throws IOException {
        repository.init();
        Well well = repository.findWellByName(name);
        if (well.getId() == 0) {
            return repository.createWell(new Well(name));
        }
        return well;
    }

    private List<String> showMenu() {
        List<String> menu = List.of(
                "Создание оборудования на скважине",
                "Информация по скважинам",
                "Экспорт данных",
                "Выход"
        );
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i));
        }
        return menu;
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SQLException {
        Scanner scanner = new Scanner(System.in);
        Repository repository = new Repository();
        new Main().init(scanner, repository);
    }
}
