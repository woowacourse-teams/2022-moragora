import { rest } from 'msw';
import GeolocationPage from '.';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';

export default {
  title: 'Pages/GeolocationPage',
  component: GeolocationPage,
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <GeolocationPage {...args} />
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
