package co.edu.icesi.VirtualStore.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserErrorCode {

    CODE_01("User ID doesn't exist."),
    CODE_02("At least one of the email or phone number fields must be entered."),
    CODE_03("Email, phone number or password are incorrect."),
    CODE_05("The email and/or phone number is already registered."),

    CODE_06("Password must have at least 1 upper case letter, 1 lower case letter, a number and a symbol like #$%@"),
    CODE_07("You must be authenticated to perform this request."),
    UNIVERSAL_ANNOTATION_CODE("Method argument not valid.");

    private final String message;
}