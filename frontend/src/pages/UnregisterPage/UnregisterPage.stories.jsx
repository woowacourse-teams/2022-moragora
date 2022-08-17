import { rest } from 'msw';
import UnregisterPage from '.';
import Header from 'components/layouts/Header';
import AppLayout from 'components/layouts/AppLayout';

export default {
  title: 'Pages/UnregisterPage',
  component: UnregisterPage,
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <UnregisterPage {...args} />
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
