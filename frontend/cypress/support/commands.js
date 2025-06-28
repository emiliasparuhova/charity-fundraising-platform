import 'cypress-file-upload';

Cypress.Commands.add('login', (email, password) => {
  cy.visit('/login');
  cy.get('input[name="email"]').type(email);
  cy.get('input[name="plainTextPassword"]').type(password);
  cy.get('button[type="submit"]').click();
});
