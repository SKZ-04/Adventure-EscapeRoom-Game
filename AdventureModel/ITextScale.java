package AdventureModel;

import java.io.Serializable;
import java.util.List;

import javafx.scene.control.Label;

public interface ITextScale extends Serializable {
    List<Label> getScalableText();
}
