package com.tam.services;

import com.tam.model.*;
import com.tam.utils.ThreatGenerationServiceHelperUtil;
import org.springframework.stereotype.Service;

@Service
class ThreatGenerationService {

    /* Threats were created with the "Elevation of Privilege Card Game" from Microsoft.
     * The game is available at https://www.microsoft.com/en-us/download/details.aspx?id=20303
     * It is licensed under the Creative Commons Attribution 3.0 United States License.
     * Native files of the game are made available to allow editing, localization, and printing of the game.
     * To view the full content of the license, visit http://creativecommons.org/licenses/by/3.0/us/
     * */
    StrideThreatResource getPortSquatThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Prevent Port Squatting")
                .description("An attacker could squat on the port or socket that " + name + " normally uses.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getCredentialsRetryThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Attacker Slow Down")
                .description("If there is nothing in place to slow an attacker down (online or offline), " +
                        "they could try one credential after another to authenticate with " + name +
                        ".")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getMissingAuthenticationThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Missing Authentication")
                .description("If no authentication mechanism is in place, an Attacker might be able to anonymously connect to " + name +
                        ".")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getClientDoesNotStoreIdentifiersThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Storing Identifiers")
                .description("If identifiers aren't stored on the client " + name + " and checked for consistency on re-connection, " +
                        "an attacker might be able to spoof a server.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getCredentialThiefThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Stealing of Credentials")
                .description("Make sure any connection with " + name + " is properly secured, so no sensitive data can be acquired by an attacker.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getPasswordReuseThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Password Reuse")
                .description("If an attacker gets hold of the authentication data used to access " + name + " and this data is not changed consistently, " +
                        "an attacker can reuse it.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getPasswordRecoveryThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.SPOOFING)
                .title("Account Recovery")
                .description("Make sure that any account recovery does not require disclosing the old password." +
                        " Otherwise, an attacker could go after the way credentials are updated or recovered.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getCustomCryptoThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Custom Crypto")
                .description("An attacker might be able to take advantage of your custom key exchange or integrity protocol when communicating with " + name + ". " +
                        "Consider using existing communication protocols with accepted cryptography standards.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getCustomCommunicationProtocolThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Replay Attacks")
                .description("When utilizing a custom communication protocol which sends packets without sequence numbers or timestamps, " +
                        "these packets might be intercepted by an attacker and replayed, resulting in a Replay Attack. Consider using an existing communication" +
                        "protocol which mitigates this threat.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataStoreWriteThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Weak Data Store Protection")
                .description("Make sure an attacker is not able to write to " + name + ".")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getCanonicalNamesThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Canonical Names")
                .description("Make sure you make names canonical before checking access permission. Otherwise an attacker might be able to bypass permissions.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataManipulationThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Data Integrity")
                .description("An attacker might be able to manipulate data because there's no integrity protection for data on the network.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataStoreACLThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Access Groups")
                .description("An attacker might be able to alter information in " + name + " because it has weak ACLs or includes a group which is equivalent to everyone.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getParameterChangeThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.TAMPERING)
                .title("Changing Parameters")
                .description("An attacker might be able to change parameters in data passed to " + name +" , even after validation (e.g. important parameters in" +
                        "a hidden field i HTML, or passing a pointer to critical memory.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

     StrideThreatResource getLogReaderAttackThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.REPUDIATION)
                .title("Log Reader Attack")
                .description("An Attacker might be able to pass data through the logs stored in " + name + " to attack a log reader. Make sure to validate logs.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getLogInfoThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.REPUDIATION)
                .title("Security Information in Logs")
                .description("An attacker might be able to acquire information about your systems structure through the logs stored in " +
                        name + ".")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getWeakDigitalSignatureThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.REPUDIATION)
                .title("Weak Signature System")
                .description("An attacker might be able to alter files or messages because the digital system you're implementing is weak (e.g. uses MACs where is should use a signature).")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getLogTimeStampThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.REPUDIATION)
                .title("Log Entry Without Timestamp")
                .description("An attacker might be able to create a log entry without a timestamp (or no log entry is timestamped at all).")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getArbitraryLogDataThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.REPUDIATION)
                .title("Arbitrary log Data")
                .description("An attacker might be able to get arbitrary data into logs from unauthenticated (or weakly authenticated) outsiders without validation.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getNoLogsThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.REPUDIATION)
                .title("No Logs")
                .description("You should implement a log system, to minimize the risk of an attack not being noticed.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataStoreNotEncryptedThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.INFORMATION_DISCLOSURE)
                .title("Missing Encryption")
                .description("An attacker might be able to access sensitive data if they get access to " + name + ". Consider encrypting the data.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataStoreEncryptedThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.INFORMATION_DISCLOSURE)
                .title("Weak Encryption")
                .description("Make sure to implement a defense (e.g. password stretching) for an attacker trying to brute-force the file encryption.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataFlowInterceptThreat(ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.INFORMATION_DISCLOSURE)
                .title("Data Flow Interception")
                .description("An attacker might be able to read a messages content if it's not encrypted.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getManInTheMiddleThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.INFORMATION_DISCLOSURE)
                .title("Man in the Middle")
                .description("Because you don't authenticate the entities connecting to " + name + ", an attacker might be able to act as a 'man in the middle' and intercept passed data.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getEncryptionKeyDiscoveryThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.INFORMATION_DISCLOSURE)
                .title("Key Disclosure")
                .description("An attacker might be able to discover the fixed key used to encrypt data on " + name + ".")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDDoSThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.DENIAL_OF_SERVICE)
                .title("DDoS")
                .description("Make sure to implement DDoS protection on " + name + ", otherwise an attacker might be able to make your system unusable." +
                        " This is especially important for any authentication mechanisms. Additionally, make sure " + name + " can't be used to amplify attacks on other parts of the system.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getDataCheckThreat(String interactorName, String processName, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.ELEVATION_OF_PRIVILEGE)
                .title("Input Control")
                .description("Ensure an attacker is not able to enter data on " + interactorName + " which is validated while still under the attacker's control.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getXSSThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.ELEVATION_OF_PRIVILEGE)
                .title("XSS")
                .description("An attacker might be able to reflect input back to " + name + ", like cross-site scripting.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getUserContentThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.ELEVATION_OF_PRIVILEGE)
                .title("User Generated Content")
                .description("You might include user-generated content within your application " + name + ", possibly including the content of random URLs.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }

    StrideThreatResource getInputSanitizationThreat(String name, ThreatMetaData threatMetaData) {
        return new StrideThreatResource()
                .threatCategory(STRIDEResource.ELEVATION_OF_PRIVILEGE)
                .title("Input Sanitization")
                .description("Make sure to sanitize the data passed to " + name + ", otherwise an attacker might be able to inject a command that the system will" +
                        "run on a higher privilege level. Client-sided validation is not enough to prevent this.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
    }
}
