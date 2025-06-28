describe('Create Charity Tests', () => {
    const charity = {
        title: 'Elephant Conservation Fund',
        description: 'Our mission is to protect and preserve elephants.',
        fundraisingGoal: 10000,
        category: 'ANIMALS',
        paypalEmail: 'donate@elephants.org',
        photos: ['photo1.jpeg', 'photo2.jpeg'],
    };

    beforeEach(() => {
        cy.login('johndoe@example.com', 'strongPassword123');
        cy.wait(2000);
        cy.visit('/create-charity');
    });

    it('should fill the form and go through all steps', () => {
        cy.get('input[name="title"]').should('be.visible').type(charity.title);
        cy.get('textarea[name="description"]').should('be.visible').type(charity.description);
        cy.get('button').contains('Next Step').should('not.be.disabled').click();

        cy.get('input[name="fundraisingGoal"]').clear().type(charity.fundraisingGoal);
        cy.get('select[name="category"]').select(charity.category);
        cy.get('button').contains('Next Step').should('not.be.disabled').click();

        cy.get('input[name="paypalEmail"]').type(charity.paypalEmail);
        cy.get('button').contains('Next Step').should('not.be.disabled').click();

        cy.get('input[type="file"]').attachFile(charity.photos[0]);
        cy.get('input[type="file"]').attachFile(charity.photos[1]);
        cy.wait(1000)
        cy.get('button').contains('Create Charity').click();

        cy.wait(1000)
        cy.url().should('eq', 'http://localhost:5173/');
    });
});
