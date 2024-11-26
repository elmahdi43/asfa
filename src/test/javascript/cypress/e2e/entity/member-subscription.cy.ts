import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('MemberSubscription e2e test', () => {
  const memberSubscriptionPageUrl = '/member-subscription';
  const memberSubscriptionPageUrlPattern = new RegExp('/member-subscription(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const memberSubscriptionSample = { subscriptionDate: '2024-11-26' };

  let memberSubscription;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/member-subscriptions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/member-subscriptions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/member-subscriptions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (memberSubscription) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/member-subscriptions/${memberSubscription.id}`,
      }).then(() => {
        memberSubscription = undefined;
      });
    }
  });

  it('MemberSubscriptions menu should load MemberSubscriptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('member-subscription');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MemberSubscription').should('exist');
    cy.url().should('match', memberSubscriptionPageUrlPattern);
  });

  describe('MemberSubscription page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(memberSubscriptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MemberSubscription page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/member-subscription/new$'));
        cy.getEntityCreateUpdateHeading('MemberSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberSubscriptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/member-subscriptions',
          body: memberSubscriptionSample,
        }).then(({ body }) => {
          memberSubscription = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/member-subscriptions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/member-subscriptions?page=0&size=20>; rel="last",<http://localhost/api/member-subscriptions?page=0&size=20>; rel="first"',
              },
              body: [memberSubscription],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(memberSubscriptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MemberSubscription page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('memberSubscription');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit MemberSubscription page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MemberSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit MemberSubscription page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MemberSubscription');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberSubscriptionPageUrlPattern);
      });

      it('last delete button click should delete instance of MemberSubscription', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('memberSubscription').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', memberSubscriptionPageUrlPattern);

        memberSubscription = undefined;
      });
    });
  });

  describe('new MemberSubscription page', () => {
    beforeEach(() => {
      cy.visit(`${memberSubscriptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MemberSubscription');
    });

    it('should create an instance of MemberSubscription', () => {
      cy.get(`[data-cy="subscriptionDate"]`).type('2024-11-26');
      cy.get(`[data-cy="subscriptionDate"]`).blur();
      cy.get(`[data-cy="subscriptionDate"]`).should('have.value', '2024-11-26');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        memberSubscription = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', memberSubscriptionPageUrlPattern);
    });
  });
});
