import { rest } from 'msw';
import SettingsPage from '.';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';

export default {
  title: 'Pages/SettingsPage',
  component: SettingsPage,
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <SettingsPage {...args} />
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
