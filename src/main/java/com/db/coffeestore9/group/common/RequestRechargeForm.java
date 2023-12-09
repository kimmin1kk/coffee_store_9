package com.db.coffeestore9.group.common;

import java.util.List;

/**
 *
 * @param amount -> 충전금액 넘기면 됨
 * @param usernames -> 주문에 참여하는 인원이 들어와야 함
 * @param groupSeq -> 그룹 pk
 */
public record RequestRechargeForm(Integer amount, List<String> usernames, Long groupSeq) {

}
