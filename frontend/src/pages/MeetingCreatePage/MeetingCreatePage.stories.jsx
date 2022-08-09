import { rest } from 'msw';
import MeetingCreatePage from '.';
import Header from 'components/layouts/Header';
import DeviceLayout from 'components/layouts/DeviceLayout';

export default {
  title: 'Pages/MeetingCreatePage',
  component: MeetingCreatePage,
  parameters: {
    reactRouter: {
      routePath: '/meeting/create',
    },
  },
};

const Template = (args) => {
  return (
    <DeviceLayout>
      <Header />
      <MeetingCreatePage {...args} />
    </DeviceLayout>
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
