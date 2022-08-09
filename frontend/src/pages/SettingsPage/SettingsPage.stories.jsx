import { rest } from 'msw';
import SettingsPage from '.';
import Header from 'components/layouts/Header';
import DeviceLayout from 'components/layouts/DeviceLayout';

export default {
  title: 'Pages/SettingsPage',
  component: SettingsPage,
};

const Template = (args) => {
  return (
    <DeviceLayout>
      <Header />
      <SettingsPage {...args} />
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
