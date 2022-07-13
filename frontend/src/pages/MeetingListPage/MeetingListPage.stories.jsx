import React from 'react';
import { rest } from 'msw';
import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import MeetingListPage from '.';

export default {
  title: 'Pages/MeetingListPage',
  component: MeetingListPage,
};

const Template = (args) => {
  return (
    <>
      <MobileLayout>
        <Header />
        <MeetingListPage {...args} />
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
