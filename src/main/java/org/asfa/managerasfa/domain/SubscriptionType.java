package org.asfa.managerasfa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A SubscriptionType.
 */
@Entity
@Table(name = "subscription_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "label", length = 100, nullable = false)
    private String label;

    @Size(max = 255)
    @Column(name = "summary", length = 255)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "members", "types", "products", "payment" }, allowSetters = true)
    private MemberSubscription subscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public SubscriptionType label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSummary() {
        return this.summary;
    }

    public SubscriptionType summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public MemberSubscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(MemberSubscription memberSubscription) {
        this.subscription = memberSubscription;
    }

    public SubscriptionType subscription(MemberSubscription memberSubscription) {
        this.setSubscription(memberSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionType)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionType{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", summary='" + getSummary() + "'" +
            "}";
    }
}
