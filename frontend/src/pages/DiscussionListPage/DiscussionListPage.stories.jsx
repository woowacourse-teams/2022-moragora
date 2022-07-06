import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import DiscussionListPage from '.';

export default {
  title: 'Pages/DiscussionListPage',
  component: DiscussionListPage,
};

const Template = (args) => {
  return (
    <MobileLayout>
      <Header />
      <DiscussionListPage {...args} />
    </MobileLayout>
  );
};

export const Default = Template.bind({});
