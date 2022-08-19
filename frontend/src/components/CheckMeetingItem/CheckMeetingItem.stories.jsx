import { css } from '@emotion/react';
import CheckMeetingItem from '.';

export default {
  title: 'Components/CheckMeetingItem',
  component: CheckMeetingItem,
};

const Template = (args) => {
  return (
    <div
      css={css`
        width: 10rem;
        background-color: lightgray;
        padding: 1rem;
      `}
    >
      <CheckMeetingItem {...args}>{'meeting'}</CheckMeetingItem>
    </div>
  );
};

export const Default = Template.bind({});
Default.args = {
  onClick: () => {},
  meeting: {
    id: 1,
    name: '리액트 사랑방',
    isActive: false,
    tardyCount: 3,
    isLoginUserMaster: true,
    isCoffeeTime: false,
    upcomingEvent: null,
  },
  clicked: false,
};
