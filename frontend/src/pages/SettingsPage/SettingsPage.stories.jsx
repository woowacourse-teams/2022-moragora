import { rest } from 'msw';
import SettingsPage from '.';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';
import { Body } from 'components/layouts/Body/Body.styled';

export default {
  title: 'Pages/SettingsPage',
  component: SettingsPage,
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <Body>
        <SettingsPage {...args} />
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
