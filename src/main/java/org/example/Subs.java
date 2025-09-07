package org.example;

public class Subs {
    private String name;
    private Long chatId;
    public Subs(Long chatId, String name) {
        this.name = name;
        this.chatId = chatId;
    }
    public String getName() {
        return name;
    }
    public Long getChatId() {
        return chatId;
    }

    @Override
    public int hashCode() {
        return this.chatId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.chatId.equals(((Subs) obj).chatId);
    }
}

