import { css } from '@emotion/react';
import UserItem from '.';

export default {
  title: 'Components/UserItem',
  component: UserItem,
};

const Template = (args) => {
  return (
    <div
      css={css`
        width: 20rem;
      `}
    >
      <UserItem {...args} />
    </div>
  );
};

export const Default = Template.bind({});
Default.args = {
  user: {
    id: 1,
    email: `user1@google.com`,
    password: `user1pw!`,
    nickname: `user1`,
    accessToken: null,
    tardyCount: 3,
  },
};
