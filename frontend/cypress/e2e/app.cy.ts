describe('유저', () => {
  beforeEach(() => {
    console.log(process.env.API_SERVER_HOST);
    cy.clearLocalStorage('accessToken');
    cy.visit('/');
  });

  describe('회원가입', () => {
    const newUser = {
      email: 'newuser@google.com',
      password: 'newuserpw1!',
      passwordConfirm: 'newuserpw1!',
      nickname: '신규회원',
    };

    it('성공 시 참여 중인 모임 목록 페이지로 이동한다.', () => {
      cy.get('a[class*="-RegisterLink"]').should('exist').click();
      cy.get('input[name="email"]').type(newUser.email);
      cy.get('button[class*="-EmailCheckButton"]').click();
      cy.get('input[name="password"]').type(newUser.password);
      cy.get('input[name="passwordConfirm"]').type(newUser.passwordConfirm);
      cy.get('input[name="nickname"]').type(newUser.nickname).type('{enter}');
      cy.url().should('include', '/meeting');
    });
  });

  describe('로그인', () => {
    const validUser = {
      email: 'user1@google.com',
      password: 'user2pw!',
    };
    const invalidUser = {
      email: 'user1@google.com',
      password: 'user2pw!',
    };

    it('성공 시 참여 중인 모임 목록 페이지로 이동한다.', () => {
      cy.get('input[name="email"]').type(validUser.email);
      cy.get('input[name="password"]').type(validUser.password).type('{enter}');
      cy.url().should('include', '/meeting');
    });

    it('실패 시 로그인 실패 경고창을 보여준다.', () => {
      cy.get('input[name="email"]').type(invalidUser.email);
      cy.get('input[name="password"]')
        .type(invalidUser.password)
        .type('{enter}');
      cy.on('window:alert', (text) => {
        expect(text).to.contains('로그인을 실패했습니다.');
      });
    });
  });

  describe('로그아웃', () => {
    const validUser = {
      email: 'user1@google.com',
      password: 'user1pw!',
    };

    it('성공 시 참여 중인 모임 목록 페이지로 이동한다.', () => {
      cy.get('input[name="email"]').type(validUser.email);
      cy.get('input[name="password"]').type(validUser.password).type('{enter}');
      cy.get('a[href="/settings"]').click();
      cy.get('button[class*="-StyledButton"]').click();
      cy.get('button[class*="-Button"]').contains('확인').click();
      cy.url().should('include', '/login');
    });
  });
});

describe('모임', () => {
  beforeEach(() => {
    console.log(process.env.API_SERVER_HOST);
    cy.clearLocalStorage('accessToken');
    cy.visit('/');
  });

  describe('생성', () => {
    const newUser = {
      email: 'newuser@google.com',
      password: 'newuserpw1!',
      passwordConfirm: 'newuserpw1!',
      nickname: '신규회원',
    };
    const newMeeting = {
      name: '새로운 모임',
      member: ['user1', 'user2'],
    };

    it('성공 시 일정 설정 페이지로 이동한다.', () => {
      cy.get('a[class*="-RegisterLink"]').should('exist').click();
      cy.get('input[name="email"]').type(newUser.email);
      cy.get('button[class*="-EmailCheckButton"]').click();
      cy.get('input[name="password"]').type(newUser.password);
      cy.get('input[name="passwordConfirm"]').type(newUser.passwordConfirm);
      cy.get('input[name="nickname"]').type(newUser.nickname).type('{enter}');
      cy.url().should('include', '/meeting');
      cy.get('a[class*="-MeetingCreateLink"]').click();
      cy.get('input[name="name"]').type(newMeeting.name);
      cy.get('input[name="searchMember"]').type(newMeeting.member[0]);
      cy.get('ul[class*="-QueryResultList"] > li').first().click();
      cy.get('input[name="searchMember"]').type(newMeeting.member[1]);
      cy.get('ul[class*="-QueryResultList"] > li').first().click();
      cy.get('button[class*="-MeetingCreateButton"]').click();
      cy.url().should('include', '/config');
    });
  });
});
