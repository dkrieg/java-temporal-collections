package example;

import com.rifftech.temporal.collections.ConcurrentSkipListTemporalCollection;
import com.rifftech.temporal.collections.MutableTemporalCollection;
import com.rifftech.temporal.collections.TemporalRecord;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.MAX;
import static com.rifftech.temporal.collections.TemporalRange.MIN;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AccountLedger {
    MutableTemporalCollection<Integer> balance = new ConcurrentSkipListTemporalCollection<>();
    int openingBalance;

    public void deposit(Instant when, int amount) {
        Assert.state(amount > 0, "cannot deposit a negative amount");
        balance.effectiveAsOf(when, amount);
    }

    public void withdraw(Instant when, int amount) {
        Assert.state(amount > 0, "cannot withdraw a negative amount");
        balance.effectiveAsOf(when, -amount);
    }

    public int balanceAsOf(Instant when) {
        return balance.getInRange(fromTo(MIN, when))
                .stream()
                .map(TemporalRecord::value)
                .reduce(openingBalance, Integer::sum);
    }

    public int currentBalance() {
        return balanceAsOf(Instant.now());
    }

    public int availableBalance() {
        return balanceAsOf(MAX);
    }

    public static void main(String[] args) {
        Instant now = Instant.now();
        Instant sixDaysAgo = now.minus(Duration.ofDays(6));
        Instant fiveDaysAgo = now.minus(Duration.ofDays(5));
        Instant fourDaysAgo = now.minus(Duration.ofDays(4));
        Instant threeDaysAgo = now.minus(Duration.ofDays(3));
        Instant twoDaysAgo = now.minus(Duration.ofDays(2));
        Instant oneDayAgo = now.minus(Duration.ofDays(1));
        Instant oneDayFromNow = now.plus(Duration.ofDays(1));

        AccountLedger ledger = new AccountLedger(100);

        // cleared transactions
        ledger.deposit(fiveDaysAgo, 100);
        ledger.withdraw(fourDaysAgo, 50);
        ledger.withdraw(twoDaysAgo, 25);
        ledger.deposit(oneDayAgo, 100);

        // pending transaction
        ledger.withdraw(oneDayFromNow, 100);

        System.out.println("balance 6 days ago should be 100.  balance = " + ledger.balanceAsOf(sixDaysAgo));
        System.out.println("balance 5 days ago should be 200.  balance = " + ledger.balanceAsOf(fiveDaysAgo));
        System.out.println("balance 4 days ago should be 150.  balance = " + ledger.balanceAsOf(fourDaysAgo));
        System.out.println("balance 3 days ago should be 150.  balance = " + ledger.balanceAsOf(threeDaysAgo));
        System.out.println("current balance should be 225.  balance = " + ledger.currentBalance());
        System.out.println("available balance should be 125.  balance = " + ledger.availableBalance());
    }
}
