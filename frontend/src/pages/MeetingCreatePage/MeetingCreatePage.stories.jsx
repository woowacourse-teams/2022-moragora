import { rest } from 'msw';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';
import { Body } from 'components/layouts/Body/Body.styled';
import MeetingCreatePage from '.';

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
    <AppLayout>
      <Header />
      <Body>
        <MeetingCreatePage {...args} />
      </Body>
      <Footer />
    </AppLayout>
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
