package co.edu.icesi.VirtualStore.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemErrorCode {

    CODE_01("Item name already exists"),
    CODE_02("Item ID doesn't exist"),
    CODE_03("Select an attribute");

    private final String message;
}
