import { rest } from 'msw';
import MeetingPage from '.';
import Header from 'components/layouts/Header';
import DeviceLayout from 'components/layouts/DeviceLayout';

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
    <DeviceLayout>
      <Header />
      <MeetingPage {...args} />
      <div id="root-modal" />
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
