import { rest } from 'msw';
import EventCreatePage from '.';
import MobileLayout from 'components/layouts/MobileLayout';
import Header from 'components/layouts/Header';
import { CalendarProvider } from 'contexts/calendarContext';

export default {
  title: 'Pages/EventCreatePage',
  component: EventCreatePage,
  parameters: {
    reactRouter: {
      routePath: '/meeting/:id/config',
      routeParams: { id: '1' },
    },
  },
};

const Template = (args) => {
  return (
    <>
      <MobileLayout>
        <Header />
        <CalendarProvider initialDate={new Date()}>
          <EventCreatePage {...args} />
        </CalendarProvider>
      </MobileLayout>
    </>
  );
};

export const Default = Template.bind({});
Default.parameters = {};

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
