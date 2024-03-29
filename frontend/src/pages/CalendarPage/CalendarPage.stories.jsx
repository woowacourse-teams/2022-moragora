import { rest } from 'msw';
import Header from 'components/layouts/Header';
import { CalendarProvider } from 'contexts/calendarContext';
import AppLayout from 'components/layouts/AppLayout';
import Footer from 'components/layouts/Footer';
import Body from 'components/layouts/Body';
import CalendarPage from '.';

export default {
  title: 'Pages/CalendarPage',
  component: CalendarPage,
  parameters: {
    reactRouter: {
      routePath: '/meeting/:id/calendar',
      routeParams: { id: '1' },
    },
  },
};

const Template = (args) => {
  return (
    <AppLayout>
      <Header />
      <Body>
        <CalendarProvider initialDate={new Date()}>
          <CalendarPage {...args} />
        </CalendarProvider>
      </Body>
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
