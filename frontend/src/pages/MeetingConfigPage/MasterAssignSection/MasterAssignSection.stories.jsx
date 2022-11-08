import { css } from '@emotion/react';
import { AuthProvider } from 'types/userType';
import MasterAssignSection from '.';

export default {
  title: 'Components/MasterAssignSection',
  component: MasterAssignSection,
};

const Template = (args) => {
  return (
    <div
      css={css`
        width: 100%;
      `}
    >
      <MasterAssignSection {...args} />
    </div>
  );
};

export const Default = Template.bind({});
Default.args = {
  meeting: {
    id: 1,
    name: '리액트 사랑방',
    isLoginUserMaster: true,
    isCoffeeTime: true,
    isActive: true,
    users: [
      {
        id: 1,
        email: 'user1@google.com',
        password: 'user1pw!',
        nickname: 'user1',
        accessToken: null,
        authProvider: AuthProvider['checkmate'],
        isMaster: true,
      },
      {
        id: 2,
        email: 'user2@google.com',
        password: 'user2pw!',
        nickname: 'user2',
        accessToken: null,
        authProvider: AuthProvider['checkmate'],
        isMaster: false,
      },
      {
        id: 3,
        email: 'user3@google.com',
        password: 'user3pw!',
        nickname: 'user3',
        accessToken: null,
        authProvider: AuthProvider['checkmate'],
        isMaster: false,
      },
      {
        id: 4,
        email: 'user4@google.com',
        password: 'user4pw!',
        nickname: 'user4',
        accessToken: null,
        authProvider: AuthProvider['checkmate'],
        isMaster: false,
      },
      {
        id: 5,
        email: 'user5@google.com',
        password: 'user5pw!',
        nickname: 'user5',
        accessToken: null,
        authProvider: AuthProvider['checkmate'],
        isMaster: false,
      },
    ],
    attendedEventCount: 10,
    masterId: 1,
  },
};
