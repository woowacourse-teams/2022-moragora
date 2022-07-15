import React from 'react';
import { rest } from 'msw';
import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import MeetingPage from '.';
import { withRouter } from 'storybook-addon-react-router-v6';

export default {
  title: 'Pages/MeetingPage',
  component: MeetingPage,
  decorators: [withRouter],
  parameters: {
    reactRouter: {
      routePath: '/meeting/:id',
      routerParams: { id: '1' },
    },
  },
};

const Template = (args) => {
  return (
    <>
      <MobileLayout>
        <Header />
        <MeetingPage {...args} />
        <div id="root-modal" />
      </MobileLayout>
    </>
  );
};

export const Default = Template.bind({});

export const Failure = Template.bind({});
Failure.parameters = {
  msw: {
    handlers: [
      rest.get('*', (req, res, ctx) => {
        return res(ctx.status(400), ctx.delay(700));
      }),
    ],
  },
};
