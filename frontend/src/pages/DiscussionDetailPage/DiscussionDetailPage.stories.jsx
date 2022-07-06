import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import DiscussionDetailPage from '.';

export default {
  title: 'Pages/DiscussionDetailPage',
  component: DiscussionDetailPage,
};

const Template = (args) => {
  return (
    <MobileLayout>
      <Header />
      <DiscussionDetailPage {...args} />
    </MobileLayout>
  );
};

export const Default = Template.bind({});
