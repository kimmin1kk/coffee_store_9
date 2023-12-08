package com.db.coffeestore9.group.common;

import java.util.List;

public record CreateGroupCardForm(String adminUsername,String groupName, List<String> usernames) {
}
