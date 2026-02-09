package org.sopt.confeti.global.transaction;

import java.util.function.Supplier;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TxRunner {

    @Transactional(propagation = Propagation.REQUIRED)
    public void runTx(Runnable runnable) {
        runnable.run();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T runTx(Supplier<T> supplier) {
        return supplier.get();
    }

    @ReadOnlyTransactional
    public <T> T runReadOnly(Supplier<T> supplier) {
        return supplier.get();
    }
}
