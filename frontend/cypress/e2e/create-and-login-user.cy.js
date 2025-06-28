describe('Authentication Tests', () => {
  const user = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'johndoe@example.com',
    password: 'strongPassword123',
  };

  it('should register a new user', () => {
    cy.visit('/register');

    cy.get('input[name="firstName"]').type(user.firstName);
    cy.get('input[name="lastName"]').type(user.lastName);
    cy.get('input[name="email"]').type(user.email);
    cy.get('input[name="password"]').type(user.password);

    cy.get('input#terms').check();

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/login');
  });

  it('should log in the user', () => {
    cy.login(user.email, user.password);

    cy.url().should('eq', 'http://localhost:5173/');
  });
});
