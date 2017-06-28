package pt.fabm;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import pt.fabm.model.AppClient;
import pt.fabm.model.Client;
import pt.fabm.model.Order;
import pt.fabm.model.OrderDetail;
import pt.fabm.model.OrderState;
import pt.fabm.model.Table;
import pt.fabm.model.TempClient;

public class TestYaml {
	@Test
	public void testYaml() throws URISyntaxException, IOException {
		Representer representer = new Representer();
		representer.addClassTag(AppClient.class, new Tag("!AppClient"));
		representer.addClassTag(Table.class, new Tag("!Table"));
		representer.addClassTag(TempClient.class, new Tag("!TempClient"));

		Constructor constructor = new Constructor();
		constructor.addTypeDescription(new TypeDescription(AppClient.class, "!AppClient"));
		constructor.addTypeDescription(new TypeDescription(Table.class, "!Table"));
		constructor.addTypeDescription(new TypeDescription(TempClient.class, "!TempClient"));

		URI uri = TestYaml.class.getResource("test.yaml").toURI();

		Yaml yaml = new Yaml(constructor, representer);
		// Object obj =
		// yaml.load(LibraryTest.class.getResourceAsStream("test.yaml"));

		List<Client> clients = new ArrayList<>();

		AppClient client1 = new AppClient();
		client1.setId(1);
		client1.setIdApp(1L);

		List<Order> orders1 = new ArrayList<>();
		Order order1 = new Order();
		order1.setOrderState(OrderState.PREPEARED);
		order1.setId(1);

		List<OrderDetail> orderDetails = new ArrayList<>();
		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setName("banana");
		orderDetail1.setId(1);
		orderDetail1.setQuantity(2);
		orderDetail1.setPrice(100);
		orderDetails.add(orderDetail1);

		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setName("sopa");
		orderDetail2.setId(2);
		orderDetail2.setQuantity(2);
		orderDetails.add(orderDetail2);

		order1.setOrderDetails(orderDetails);
		orders1.add(order1);

		client1.setOrders(orders1);

		TempClient client2 = new TempClient();
		client2.setName("1");
		client2.setId(2);

		clients.add(client1);
		clients.add(client2);

		Table table = new Table();
		table.setId(1);
		table.setClients(clients);

		// FileWriter writer = new FileWriter(new File(uri));
		// yaml.dump(table, writer);

		File file = new File(uri);
		System.out.println("exists:" + file.exists());
		System.out.println("tamanho:" + file.length());
		System.out.println("path:" + file.getAbsolutePath());

		String content = new String(Files.readAllBytes(Paths.get(uri)));

		Table tableDump = yaml.loadAs(content, Table.class);
		Assert.assertEquals(tableDump.getClients().get(1).getId(), 2L);
		TempClient tempClient = (TempClient) tableDump.getClients().get(1);
		Assert.assertEquals(tempClient.getName(), "2");
		Assert.assertEquals(tempClient.getId(), 2L);

		System.out.println(yaml.dump(table));
	}


}