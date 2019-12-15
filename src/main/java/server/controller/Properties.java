package server.controller;

public enum Properties {
    Authentication,
    Registration,
    MessageForAll,
    PrivateMessage,
    ActiveUsers,
    Ban,
    BanUsers,
    Edit,
    Remove,
    Close,
    Admin,
    UnBan,
    IncorrectValue,
    Stop,
    Successfully;

    public static Properties fromString(String command) {
        if (command != null) {
            for (Properties properties : Properties.values()) {
                if (properties.name().equals(command)) {
                    return properties;
                }
            }

        }
        return null;
    }
}


