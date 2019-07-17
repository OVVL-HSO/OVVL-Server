package com.tam.validator;

import org.passay.*;

import java.util.Arrays;

public interface PasswordValidator {

    static boolean isValid(String password) {
        org.passay.PasswordValidator generalValidator = new org.passay.PasswordValidator(Arrays.asList(
                // at least 8 characters
                new LengthRule(8, 30),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // no whitespace
                new WhitespaceRule()

        ));
        RuleResult resultOfBasicValidation = generalValidator.validate(new PasswordData(password));
        if (!resultOfBasicValidation.isValid()) {
            return false;
        }
        org.passay.PasswordValidator digitValidator = new org.passay.PasswordValidator(Arrays.asList(
                new CharacterRule(EnglishCharacterData.Digit, 1)
        ));

        RuleResult digitValidation = digitValidator.validate(new PasswordData(password));
        if (digitValidation.isValid()) {
            return true;
        }

        org.passay.PasswordValidator specialCharValidator = new org.passay.PasswordValidator(Arrays.asList(
                new CharacterRule(EnglishCharacterData.Special, 1)
        ));

        RuleResult specialCharValidation = specialCharValidator.validate(new PasswordData(password));
        return specialCharValidation.isValid();
    }
}
