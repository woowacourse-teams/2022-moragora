import React from 'react';
import { rest } from 'msw';
import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import MeetingPage from '.';

export default {
  title: 'Pages/MeetingPage',
  component: MeetingPage,
  parameters: {
    reactRouter: {
      routePath: '/meeting/:id',
      routeParams: { id: '1' },
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
