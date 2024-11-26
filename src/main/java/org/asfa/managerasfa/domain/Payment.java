package org.asfa.managerasfa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_uid")
    private UUID paymentUID;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private Instant timeStamp;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "members", "types", "products", "payment" }, allowSetters = true)
    private Set<MemberSubscription> memberSubscriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<PaymentMethod> methods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPaymentUID() {
        return this.paymentUID;
    }

    public Payment paymentUID(UUID paymentUID) {
        this.setPaymentUID(paymentUID);
        return this;
    }

    public void setPaymentUID(UUID paymentUID) {
        this.paymentUID = paymentUID;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Payment amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Instant getTimeStamp() {
        return this.timeStamp;
    }

    public Payment timeStamp(Instant timeStamp) {
        this.setTimeStamp(timeStamp);
        return this;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Set<MemberSubscription> getMemberSubscriptions() {
        return this.memberSubscriptions;
    }

    public void setMemberSubscriptions(Set<MemberSubscription> memberSubscriptions) {
        if (this.memberSubscriptions != null) {
            this.memberSubscriptions.forEach(i -> i.setPayment(null));
        }
        if (memberSubscriptions != null) {
            memberSubscriptions.forEach(i -> i.setPayment(this));
        }
        this.memberSubscriptions = memberSubscriptions;
    }

    public Payment memberSubscriptions(Set<MemberSubscription> memberSubscriptions) {
        this.setMemberSubscriptions(memberSubscriptions);
        return this;
    }

    public Payment addMemberSubscription(MemberSubscription memberSubscription) {
        this.memberSubscriptions.add(memberSubscription);
        memberSubscription.setPayment(this);
        return this;
    }

    public Payment removeMemberSubscription(MemberSubscription memberSubscription) {
        this.memberSubscriptions.remove(memberSubscription);
        memberSubscription.setPayment(null);
        return this;
    }

    public Set<PaymentMethod> getMethods() {
        return this.methods;
    }

    public void setMethods(Set<PaymentMethod> paymentMethods) {
        if (this.methods != null) {
            this.methods.forEach(i -> i.setPayment(null));
        }
        if (paymentMethods != null) {
            paymentMethods.forEach(i -> i.setPayment(this));
        }
        this.methods = paymentMethods;
    }

    public Payment methods(Set<PaymentMethod> paymentMethods) {
        this.setMethods(paymentMethods);
        return this;
    }

    public Payment addMethods(PaymentMethod paymentMethod) {
        this.methods.add(paymentMethod);
        paymentMethod.setPayment(this);
        return this;
    }

    public Payment removeMethods(PaymentMethod paymentMethod) {
        this.methods.remove(paymentMethod);
        paymentMethod.setPayment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentUID='" + getPaymentUID() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
