package org.sopt.confeti.global.transaction;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ParameterInvalidException;
import org.sopt.confeti.global.message.ErrorMessage;

@Slf4j
public final class Tx {

    private static TxRunner txRunner;

    public static void initialize(TxRunner txRunner) {
        if (Tx.txRunner != null) {
            log.error("Tx.initialize : Tx가 이미 초기화 되었습니다.");
            throw new ParameterInvalidException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }

        Tx.txRunner = txRunner;
    }

    public static void masterTx(Runnable function) {
        txRunner.runTx(function);
    }

    public static <T> T masterTx(Supplier<T> function) {
        return txRunner.runTx(function);
    }

    public static <T> T readOnlyTx(Supplier<T> function) {
        return txRunner.runReadOnly(function);
    }
}
