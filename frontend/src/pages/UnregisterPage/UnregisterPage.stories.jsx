import { rest } from 'msw';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';
import { Body } from 'components/layouts/Body/Body.styled';
import UnregisterPage from '.';

export default {
  title: 'Pages/UnregisterPage',
  component: UnregisterPage,
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <Body>
        <UnregisterPage {...args} />
      </Body>
      <Footer />
      <div id="root-modal" />
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
