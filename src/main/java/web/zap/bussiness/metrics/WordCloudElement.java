package web.zap.bussiness.metrics;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by rafa on 25/06/2016.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WordCloudElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private Long weight;
}
