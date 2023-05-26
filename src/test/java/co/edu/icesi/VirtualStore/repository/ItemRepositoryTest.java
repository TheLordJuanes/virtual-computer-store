package co.edu.icesi.VirtualStore.repository;

import co.edu.icesi.VirtualStore.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemsRepository itemsRepository;

    @Test
    public void testUpdateItemName() {
        Item itemCreate = itemsRepository.save(Item.builder().name("Mini PC").description("A mini pc.").price(2000).urlImage("https://www.img2link.com/images/2022/12/01/aaf04b258acfc1bf50e03b699b611fa7.jpg").build());
        itemsRepository.updateItemName(itemCreate.getId(), "Big PC");
        Optional<Item> itemGet = itemsRepository.findById(itemCreate.getId());
        assertThat(itemGet.isPresent()).isTrue();
        assertThat(itemGet.get().getName()).isEqualTo("Big PC");
    }

    @Test
    public void testUpdateItemDescription() {
        Item itemCreate = itemsRepository.save(Item.builder().name("Medium PC").description("A mini pc.").price(1500).urlImage("https://www.img2link.com/images/2022/12/01/aaf04b258acfc1bf50e03b699b611fa7.jpg").build());
        itemsRepository.updateItemDescription(itemCreate.getId(), "A medium pc.");
        Optional<Item> itemGet = itemsRepository.findById(itemCreate.getId());
        assertThat(itemGet.isPresent()).isTrue();
        assertThat(itemGet.get().getDescription()).isEqualTo("A medium pc.");
    }

    @Test
    public void testUpdateItemPrice() {
        Item itemCreate = itemsRepository.save(Item.builder().name("Mini PC").description("A mini pc.").price(1500).urlImage("https://www.img2link.com/images/2022/12/01/aaf04b258acfc1bf50e03b699b611fa7.jpg").build());
        itemsRepository.updateItemPrice(itemCreate.getId(), Double.parseDouble("1000"));
        Optional<Item> itemGet = itemsRepository.findById(itemCreate.getId());
        assertThat(itemGet.isPresent()).isTrue();
        assertThat(itemGet.get().getPrice()).isEqualTo(1000);
    }
}
