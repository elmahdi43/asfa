package org.asfa.managerasfa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A MemberSubscription.
 */
@Entity
@Table(name = "member_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "subscription_date", nullable = false)
    private LocalDate subscriptionDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscription")
    @JsonIgnoreProperties(value = { "familyMembers", "subscription", "member" }, allowSetters = true)
    private Set<Member> members = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscription")
    @JsonIgnoreProperties(value = { "subscription" }, allowSetters = true)
    private Set<SubscriptionType> types = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscription")
    @JsonIgnoreProperties(value = { "categories", "subscription" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "memberSubscriptions", "methods" }, allowSetters = true)
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MemberSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSubscriptionDate() {
        return this.subscriptionDate;
    }

    public MemberSubscription subscriptionDate(LocalDate subscriptionDate) {
        this.setSubscriptionDate(subscriptionDate);
        return this;
    }

    public void setSubscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Member> members) {
        if (this.members != null) {
            this.members.forEach(i -> i.setSubscription(null));
        }
        if (members != null) {
            members.forEach(i -> i.setSubscription(this));
        }
        this.members = members;
    }

    public MemberSubscription members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public MemberSubscription addMembers(Member member) {
        this.members.add(member);
        member.setSubscription(this);
        return this;
    }

    public MemberSubscription removeMembers(Member member) {
        this.members.remove(member);
        member.setSubscription(null);
        return this;
    }

    public Set<SubscriptionType> getTypes() {
        return this.types;
    }

    public void setTypes(Set<SubscriptionType> subscriptionTypes) {
        if (this.types != null) {
            this.types.forEach(i -> i.setSubscription(null));
        }
        if (subscriptionTypes != null) {
            subscriptionTypes.forEach(i -> i.setSubscription(this));
        }
        this.types = subscriptionTypes;
    }

    public MemberSubscription types(Set<SubscriptionType> subscriptionTypes) {
        this.setTypes(subscriptionTypes);
        return this;
    }

    public MemberSubscription addTypes(SubscriptionType subscriptionType) {
        this.types.add(subscriptionType);
        subscriptionType.setSubscription(this);
        return this;
    }

    public MemberSubscription removeTypes(SubscriptionType subscriptionType) {
        this.types.remove(subscriptionType);
        subscriptionType.setSubscription(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setSubscription(null));
        }
        if (products != null) {
            products.forEach(i -> i.setSubscription(this));
        }
        this.products = products;
    }

    public MemberSubscription products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public MemberSubscription addProducts(Product product) {
        this.products.add(product);
        product.setSubscription(this);
        return this;
    }

    public MemberSubscription removeProducts(Product product) {
        this.products.remove(product);
        product.setSubscription(null);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public MemberSubscription payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((MemberSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberSubscription{" +
            "id=" + getId() +
            ", subscriptionDate='" + getSubscriptionDate() + "'" +
            "}";
    }
}
