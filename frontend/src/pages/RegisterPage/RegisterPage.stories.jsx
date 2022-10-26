import { rest } from 'msw';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';
import { Body } from 'components/layouts/Body/Body.styled';
import RegisterPage from '.';

export default {
  title: 'Pages/RegisterPage',
  component: RegisterPage,
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <Body>
        <RegisterPage {...args} />
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
