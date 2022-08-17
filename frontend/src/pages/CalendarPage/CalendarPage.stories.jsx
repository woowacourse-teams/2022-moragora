import { rest } from 'msw';
import CalendarPage from '.';
import Header from 'components/layouts/Header';
import { CalendarProvider } from 'contexts/calendarContext';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';

export default {
  title: 'Pages/CalendarPage',
  component: CalendarPage,
  parameters: {
    reactRouter: {
      routePath: '/meeting/:id/events',
      routeParams: { id: '1' },
    },
  },
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <CalendarProvider initialDate={new Date()}>
        <CalendarPage {...args} />
      </CalendarProvider>
      <Footer />
    </AppLayout>
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
